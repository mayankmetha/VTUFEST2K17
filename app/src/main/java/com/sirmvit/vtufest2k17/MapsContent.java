package com.sirmvit.vtufest2k17;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


class MapsContent {

    //coordinates for buildings
    private static final LatLng LOC_MVIT = new LatLng(13.15103, 77.610929); //gate - 0
    private static final LatLng LOC_SCIENCE = new LatLng(13.150812, 77.609026); //sci block - 1
    private static final LatLng LOC_NB = new LatLng(13.150893, 77.610195); //new block - 2
    private static final LatLng LOC_NA = new LatLng(13.151428, 77.607093); // new audi - 3
    private static final LatLng LOC_OA = new LatLng(13.151408, 77.607293); // old audi - 4
    private static final LatLng LOC_LIB = new LatLng(13.151283, 77.608933); // lib - 5
    private static final LatLng LOC_PL = new LatLng(13.150273, 77.609734); // parking ground - 6
    private static final LatLng LOC_CS = new LatLng(13.151042, 77.608881); //coffee - 7
    private static final LatLng LOC_RS = new LatLng(13.149867, 77.610226); //rolls & stationary - 8
    private static final LatLng LOC_ME = new LatLng(13.150604, 77.6084); //mech block - 9
    private static final LatLng LOC_CB = new LatLng(13.150826, 77.60869); //civil - 10
    private static final LatLng LOC_MBA = new LatLng(13.151316, 77.609377); //mba - 11
    private static final LatLng LOC_DEN = new LatLng(13.149771, 77.608481); //dental - 12
    private static final LatLng LOC_WORK = new LatLng(13.151112, 77.607833); //Workshop - 13
    private static final LatLng LOC_GROUND = new LatLng(13.14973, 77.605473); //Ground - 14
    private static final LatLng LOC_SUGAR = new LatLng(13.149946,77.609871); // Sugarcane - 15
    private static final LatLng LOC_ATM = new LatLng(13.149824,77.610034);//atm - 16
    private static final LatLng LOC_CANT = new LatLng(13.149910,77.610383); //canteen - 17
    private static final LatLng LOC_BUS = new LatLng(13.1511705,77.6073934); //bus depot - 18
    private static final LatLng LOC_HT = new LatLng(13.151150, 77.608221); // washroom block - 19
    private static final LatLng LOC_AUDIBUS = new LatLng(13.150918,77.607582); //navigation for auditorium and bus depot
    private static final LatLng LOC_GROUNDNAV = new LatLng(13.149468, 77.606194);//navigation for ground
    static List<MapsItem> ITEMS;
    static List<MapsItem> HTS; //washrooms
    private static final LatLng HTS_SCIENCE = new LatLng(13.150680, 77.609000);
    private static final LatLng HTS_NB = new LatLng(13.151000, 77.609900);
    private static final LatLng HTS_ME = new LatLng(13.150700, 77.608100);
    private static final LatLng HTS_CB = new LatLng(13.151156, 77.60850);
    private static final LatLng HTS_MBA = new LatLng(13.151200, 77.609377);
    private static final LatLng HTS_HT = new LatLng(13.151100, 77.608221);


    private static void addItem(MapsItem item) {
        ITEMS.add(item);
    }
    private static void addHts(MapsItem item) { //washrooms
        HTS.add(item);
    }

    static{
        ITEMS = new ArrayList<>();
        addItem(new MapsItem(0,"Sir MVIT Entrance",LOC_MVIT,LOC_MVIT));
        addItem(new MapsItem(1,"Science Block",LOC_SCIENCE,LOC_SCIENCE));
        addItem(new MapsItem(2,"New Block",LOC_NB,LOC_NB));
        addItem(new MapsItem(3,"New Auditorium",LOC_NA,LOC_AUDIBUS));
        addItem(new MapsItem(4,"Old Auditorium",LOC_OA,LOC_AUDIBUS));
        addItem(new MapsItem(5,"Library",LOC_LIB,LOC_LIB));
        addItem(new MapsItem(6,"Parking Lot",LOC_PL,LOC_PL));
        addItem(new MapsItem(7,"Coffee Shop",LOC_CS,LOC_CS));
        addItem(new MapsItem(8,"Rolls Corner & Stationary Shop",LOC_RS,LOC_RS));
        addItem(new MapsItem(9,"Mechanical Block",LOC_ME,LOC_ME));
        addItem(new MapsItem(10,"Civil Block",LOC_CB,LOC_CB));
        addItem(new MapsItem(11,"MBA Block",LOC_MBA,LOC_MBA));
        addItem(new MapsItem(12,"Dental Block",LOC_DEN,LOC_DEN));
        addItem(new MapsItem(13,"Workshop Block",LOC_WORK,LOC_WORK));
        addItem(new MapsItem(14,"Grounds",LOC_GROUND,LOC_GROUNDNAV));
        addItem(new MapsItem(15,"Sugarcane Juice Shop",LOC_SUGAR,LOC_SUGAR));
        addItem(new MapsItem(16,"ATM",LOC_ATM,LOC_ATM));
        addItem(new MapsItem(17,"Canteen",LOC_CANT,LOC_CANT));
        addItem(new MapsItem(18,"Sir MVIT Bus Depot",LOC_BUS,LOC_AUDIBUS));
        addItem(new MapsItem(19,"Washroom",LOC_HT,LOC_HT));
        //washrooms
        HTS = new ArrayList<>();
        addHts(new MapsItem(0,"Science Block",HTS_SCIENCE,HTS_SCIENCE));
        addHts(new MapsItem(1,"New Block", HTS_NB,HTS_NB));
        addHts(new MapsItem(2,"Mechanical Block",HTS_ME,HTS_ME));
        addHts(new MapsItem(3,"Civil Block",HTS_CB,HTS_CB));
        addHts(new MapsItem(4,"MBA Block",HTS_MBA,HTS_MBA));
        addHts(new MapsItem(5,"Washroom",HTS_HT,HTS_HT));
    }
}
