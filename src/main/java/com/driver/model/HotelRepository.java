package com.driver.model;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
@Repository
public class HotelRepository {
     HashMap<String,Hotel>HotelDb=new HashMap<>();
     HashMap<Integer,User>UsersDb=new HashMap<>();
     HashMap<String,Booking> bookingDb = new HashMap<>();
     HashMap<Integer,Integer>countOfBookings=new HashMap<>();
    public String addHotel(Hotel hotel) {
       if(HotelDb.containsKey(hotel.getHotelName())){
           return "Failure";
       }
       HotelDb.put(hotel.getHotelName(),hotel);
       return "success";
    }

    public Integer addUser(User user) {
        UsersDb.put(user.getaadharCardNo(),user);
        return user.getaadharCardNo();
    }

    public void bookRoom(Booking booking, Hotel hotel) {
        String hotelname=hotel.getHotelName();
        HotelDb.put(hotelname,hotel);
        bookingDb.put(booking.getBookingId(), booking);
        int aadharCard = booking.getBookingAadharCard();
        countOfBookings.put(aadharCard,countOfBookings.getOrDefault(aadharCard,0)+1);
    }
}
