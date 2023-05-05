package com.driver.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class HotelService {
    @Autowired
    HotelRepository hotelRepository;
    public String addHotel(Hotel hotel) {
        if(hotel.getHotelName()==null || hotel==null  )
              return "Failure";
       return hotelRepository.addHotel(hotel);
    }

    public Integer addUser(User user) {
        return hotelRepository.addUser(user);
    }

    public String getHotelWithMostFacilities() {
        HashMap<String,Hotel>db=hotelRepository.HotelDb;
        String hotel="";
        int cnt=0;
        for(String it:db.keySet()){
            if(cnt < db.get(it).getFacilities().size()){
                cnt=db.get(it).getFacilities().size();
                hotel=it;
            }
            else if(cnt == db.get(it).getFacilities().size()){
                if(hotel.compareTo(it)>0) {
                    hotel=it;
                }
            }
        }
        return cnt==0?"":hotel;
    }

    public int bookARoom(Booking booking) {
        String uuid=UUID.randomUUID().toString();
        booking.setBookingId(uuid);
        String hotelname=booking.getHotelName();
        Hotel hotel=hotelRepository.HotelDb.get(hotelname);
        int availableroom=hotel.getAvailableRooms();
        if(availableroom<booking.getNoOfRooms()){
            return -1;
        }
        int amountToBePaid = hotel.getPricePerNight()*booking.getNoOfRooms();
        booking.setAmountToBePaid(amountToBePaid);
        hotel.setAvailableRooms(hotel.getAvailableRooms()-booking.getNoOfRooms());
        hotelRepository.bookRoom(booking,hotel);
        return amountToBePaid;
    };

    public int getBookings(Integer aadharCard) {
        return hotelRepository.countOfBookings.get(aadharCard);
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        Hotel hotel = hotelRepository.HotelDb.get(hotelName);
        List<Facility> oldFacilities = hotelRepository.HotelDb.get(hotelName).getFacilities();

        for(Facility facility: newFacilities){

            if(oldFacilities.contains(facility)){
                continue;
            }else{
                oldFacilities.add(facility);
            }
        }

        hotel.setFacilities(oldFacilities);

        hotelRepository.HotelDb.put(hotelName,hotel);

        return hotel;
    }
}
