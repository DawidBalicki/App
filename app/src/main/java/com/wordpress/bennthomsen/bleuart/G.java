package com.wordpress.bennthomsen.bleuart;

import android.app.Application;

/**
 * Created by Dawid on 10.12.2017.
 */

public class G extends Application{
   public static UartService mService ;
   public static String dane;


   public static UartService getmService() {
      return mService;
   }

   public static void setmService(UartService mService) {
      G.mService = mService;
   }

   public static void setDane(String dane) {
      G.dane = dane;
   }

   public static String getDane() {
      return dane;
   }
}