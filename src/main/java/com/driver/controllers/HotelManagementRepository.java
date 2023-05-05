package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Repository
public class HotelManagementRepository {

    HashMap<String,Hotel> hotelMap = new HashMap<>();
    HashMap<Integer,User> userMap = new HashMap<>();
    HashMap<Integer,Integer> userBooking = new HashMap<>();
    HashMap<String,Booking> bookingMap = new HashMap<>();


    public String addHotel(Hotel hotel){

        //You need to add an hotel to the database
        //incase the hotelName is null or the hotel Object is null return an empty a FAILURE
        //Incase somebody is trying to add the duplicate hotelName return FAILURE
        //in all other cases return hotelName after successfully adding the hotel to the hotelDb.

        if(hotel==null || hotel.getHotelName() == null)
            return "FAILURE";

        if(hotelMap.containsKey(hotel.getHotelName()))
            return "FAILURE";

        hotelMap.put(hotel.getHotelName(),hotel);
        return "SUCCESS";
    }

    public Integer addUser(User user){

        //You need to add a User Object to the database
        //Assume that user will always be a valid user and return the aadharCardNo of the user
        if(userMap.containsKey(user.getaadharCardNo()))
            return 0;

        userMap.put(user.getaadharCardNo(),user);
        return user.getaadharCardNo();
    }

    public String getHotelWithMostFacilities(){

        //Out of all the hotels we have added so far, we need to find the hotelName with most no of facilities
        //Incase there is a tie return the lexicographically smaller hotelName
        //Incase there is not even a single hotel with atleast 1 facility return "" (empty string)
        List<String> hotelNames = new ArrayList<>();
        int max = 1;
        if(hotelMap.size()==0)
            return "";
        for(Hotel hotel: hotelMap.values()){
            if(hotel.getFacilities().size()>=max)
                max = hotel.getFacilities().size();
        }
        for(Hotel hotel: hotelMap.values()){
            if(hotel.getFacilities().size()==max)
                hotelNames.add(hotel.getHotelName());
        }
        if(hotelNames.size()==1)
            return hotelNames.get(0);
        if(hotelNames.size()==0)
            return "";
        for(int i=0;i<hotelNames.size()-1;i++){
            for(int j=i+1;j<hotelNames.size();j++){
                if(hotelNames.get(i).compareTo(hotelNames.get(j))>0){
                    String temp = hotelNames.get(i);
                    hotelNames.add(i,hotelNames.get(j));
                    hotelNames.add(j,temp);
                }
            }
        }
        return hotelNames.get(0);
    }

    public int bookARoom(Booking booking){

        //The booking object coming from postman will have all the attributes except bookingId and amountToBePaid;
        //Have bookingId as a random UUID generated String
        //save the booking Entity and keep the bookingId as a primary key
        //Calculate the total amount paid by the person based on no. of rooms booked and price of the room per night.
        //If there arent enough rooms available in the hotel that we are trying to book return -1
        //in other case return total amount paid
        Hotel hotel = hotelMap.get(booking.getHotelName());
        if(hotel.getAvailableRooms()<booking.getNoOfRooms())
            return -1;

        String bookingId = UUID.randomUUID().toString();
        int amount = booking.getNoOfRooms()*hotel.getPricePerNight();
        booking.setAmountToBePaid(amount);
        booking.setBookingId(bookingId);
        bookingMap.put(bookingId,booking);
        userBooking.put(booking.getBookingAadharCard(),userBooking.getOrDefault(booking.getBookingAadharCard(),0)+1);
        return amount;
    }

    public int getBookings(Integer aadharCard)
    {
        //In this function return the bookings done by a person
        for (int userId : userBooking.keySet()) {
            if (userId == aadharCard)
                return userBooking.get(userId);
        }
        return 0;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName){

        //We are having a new facilites that a hotel is planning to bring.
        //If the hotel is already having that facility ignore that facility otherwise add that facility in the hotelDb
        //return the final updated List of facilities and also update that in your hotelDb
        //Note that newFacilities can also have duplicate facilities possible
        Hotel hotel = hotelMap.get(hotelName);
        for(Facility facility:newFacilities){
            if(hotel.getFacilities()==null){
                List<Facility> facilities = new ArrayList<>();
                hotel.setFacilities(facilities);
            }
            else if (!hotel.getFacilities().contains(facility))
                hotel.getFacilities().add(facility);
        }
        return hotel;
    }
}