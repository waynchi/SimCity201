package city.gui;
import javax.swing.*;

import transportation.BusAgent;
import transportation.BusGui;
import transportation.BusStop;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;


public class CityPanel extends JPanel implements MouseListener {
	ArrayList<Building> buildings;
	ArrayList<Lane> lanes;
	ArrayList<Sidewalk> sidewalks;
	ArrayList<Vehicle> vehicles;
	ArrayList<PersonGui> people;
	public List<BusStop> busStops;
	ArrayList<Lane> road1,road2,road3,road4,road5,road6,road7,road8,road9,road10,road11,road12,road13,road14,road15,road16,road17,road18,road19,road20,road21,road22;
	ArrayList<ArrayList<Lane>> allRoads;
	ArrayList<Sidewalk> sidewalkStrip1,sidewalkStrip2,sidewalkStrip3,sidewalkStrip4,sidewalkStrip5,sidewalkStrip6,
	sidewalkStrip7,sidewalkStrip8,sidewalkStrip9,sidewalkStrip10,sidewalkStrip11,sidewalkStrip12,sidewalkStrip13,sidewalkStrip14,sidewalkStrip15,
	sidewalkStrip16,sidewalkStrip17,sidewalkStrip18,sidewalkStrip19,sidewalkStrip20,sidewalkStrip21,sidewalkStrip22,sidewalkStrip23,sidewalkStrip24,
	sidewalkStrip25,sidewalkStrip26,sidewalkStrip27,sidewalkStrip28,sidewalkStrip29,sidewalkStrip30;
	ArrayList<ArrayList<Sidewalk>> allSidewalks;
	ArrayList<Lane> intersections;
	CityGui city;
	int count = 0;
	static final int hozX = 350;
	static final int hozY = 40;
	static final int crossX = 330;
	static final int crossY = 40;
	static final int crossWidth = 20;
	static final int crossHeight = 210;
	static final int hozWidth = 210;
	static final int hozHeight = 20;
	static final int sidewalkHeight = 10;
	static final int laneWidth = 20;
	
	
	


	public CityPanel(CityGui city) {
		buildings = new ArrayList<Building>();
		lanes = new ArrayList<Lane>();
		sidewalks = new ArrayList<Sidewalk>();
		vehicles = new ArrayList<Vehicle>();
		people = new ArrayList<PersonGui>();
		busStops = new ArrayList<BusStop>();
		this.city = city;
		
		road1 = new ArrayList<Lane>();
		road2 = new ArrayList<Lane>();
		road3 = new ArrayList<Lane>();
		road4 = new ArrayList<Lane>();
		road5 = new ArrayList<Lane>();
		road6 = new ArrayList<Lane>();
		road7 = new ArrayList<Lane>();
		road8 = new ArrayList<Lane>();
		road9 = new ArrayList<Lane>();
		road10 = new ArrayList<Lane>();
		road11 = new ArrayList<Lane>();
		road12 = new ArrayList<Lane>();
		road13 = new ArrayList<Lane>();
		road14 = new ArrayList<Lane>();
		road15 = new ArrayList<Lane>();
		road16 = new ArrayList<Lane>();
		road17 = new ArrayList<Lane>();
		road18 = new ArrayList<Lane>();
		road19 = new ArrayList<Lane>();
		road20 = new ArrayList<Lane>();
		road21 = new ArrayList<Lane>();
		road22 = new ArrayList<Lane>();
		
		allRoads = new ArrayList<ArrayList<Lane>>();
		allRoads.add(road1);
		allRoads.add(road2);
		allRoads.add(road3);
		allRoads.add(road4);
		allRoads.add(road5);
		allRoads.add(road6);
		allRoads.add(road7);
		allRoads.add(road8);
		allRoads.add(road9);
		allRoads.add(road10);
		allRoads.add(road11);
		allRoads.add(road12);
		allRoads.add(road13);
		allRoads.add(road14);
		allRoads.add(road15);
		allRoads.add(road16);
		allRoads.add(road17);
		allRoads.add(road18);
		allRoads.add(road19);
		allRoads.add(road20);
		allRoads.add(road21);
		allRoads.add(road22);

		allSidewalks = new ArrayList<ArrayList<Sidewalk>>();
		
		sidewalkStrip1 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip1);
		sidewalkStrip2 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip2);
		sidewalkStrip3 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip3);
		sidewalkStrip4 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip4);
		sidewalkStrip5 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip5);
		sidewalkStrip6 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip6);
		sidewalkStrip7 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip7);
		sidewalkStrip8 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip8);
		sidewalkStrip9 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip9);
		sidewalkStrip10 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip10);
		sidewalkStrip11 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip11);
		sidewalkStrip12 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip12);
		sidewalkStrip13 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip13);
		sidewalkStrip14 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip14);
		sidewalkStrip15 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip15);
		sidewalkStrip16 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip16);
		sidewalkStrip17 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip17);
		sidewalkStrip18 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip18);
		sidewalkStrip19 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip19);
		sidewalkStrip20 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip20);
		sidewalkStrip21 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip21);
		sidewalkStrip22 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip22);
		sidewalkStrip23 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip23);
		sidewalkStrip24 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip24);
		sidewalkStrip25 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip25);
		sidewalkStrip26 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip26);
		sidewalkStrip27 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip27);
		sidewalkStrip28 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip28);
		sidewalkStrip29 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip29);
		sidewalkStrip30 = new ArrayList<Sidewalk>();
		allSidewalks.add(sidewalkStrip30);		
		
		intersections = new ArrayList<Lane>();
		
		
		
		
		
		


		//Create grid of lanes
		
		//Center Horizontal Top Lanes
		Sidewalk s;
		for(int k = 0; k < hozWidth/10 ; k++)
		{
		s = new Sidewalk( hozX - 210 + 10*k, hozY + 130 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "1_" + k ); 
		sidewalks.add(s);
		sidewalkStrip1.add(s);
		}
		
		Lane l;
		for(int k = 0 ; k<((hozX - 210) + hozY+110)/20 ; k++)
		{
			l = new Lane( hozX - 210 + 20*k, hozY + 90, laneWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "1_" + k );
			lanes.add( l );
			road1.add(l);
		}
		
		for(int k = 0 ; k<((hozX - 210) + hozY+110)/20 ; k++)
		{
			l = new Lane( hozX - 210 + 20*k, hozY + 110, laneWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "2_" + k);
			lanes.add( l );
			road2.add(l);
			if(k == 13) {
				intersections.add(l); //First intersection
			}
		}
		
		for(int k = 0; k < hozWidth/10; k++)
		{
			s = new Sidewalk( hozX - 210 + 10*k, hozY + 80 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "2_" + k ); 
			sidewalks.add(s);
			sidewalkStrip2.add(s);
		}
		
		for(int k = 0 ; k < (hozWidth - 150)/10;k++)
		{
			s = new Sidewalk( hozX + 10*k, hozY + 130 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "3_" + k ); 
			sidewalks.add(s);
			sidewalkStrip3.add(s);
		}
		
		for(int k = 0; k < (hozWidth-140)/10; k ++)
		{
			s = new Sidewalk( hozX + 10*k, hozY + 80 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "4_" + k ); 
			sidewalks.add(s);
			sidewalkStrip4.add(s);
		}
		
		//Beginning of main city
		
		//Top of city
		for(int k = 0 ; k < (hozWidth + 120)/10 ; k ++) {
			s = new Sidewalk( hozX + 70 + 10*k, hozY - 30 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "5_" + k ); 
			sidewalks.add(s);
			sidewalkStrip5.add(s);
		}
		
		for(int k = 0 ; k<(hozWidth +140 )/20  ; k++) {
			l = new Lane( hozX + 70 + 20*k, hozY, laneWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "3_" + k );
			lanes.add( l );
			road3.add(l);
			if(k == 15) {
				intersections.add(l);
			}
			
		}
		
		for(int k = 0 ; k<(hozWidth + 150)/20 ; k++) {
			l = new Lane( hozX + 70 + 20*k, hozY - 20, laneWidth , hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "4_" + k );
			lanes.add( l );
			road4.add(l);
		
		}

		for(int k = 0 ; k < (hozWidth+50)/10 ; k ++)
		{
		s = new Sidewalk( hozX + 400 + 10*k, hozY - 30 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "6_" + k ); 
		sidewalks.add(s);
		sidewalkStrip6.add(s);
		}
		
		for(int k = 0; k<((hozWidth - 10)/20); k++)
		{
		l = new Lane( hozX + 420 + 20*k, hozY, laneWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "5_" + k );
		lanes.add( l );
		road5.add(l);
		}
		
		for(int k = 0; k <(hozWidth + 20)/20;k++)
		{
			l = new Lane( hozX + 420 + 20*k, hozY-20, laneWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "6_" + k );
			lanes.add( l );
			road6.add(l);
			if(k == 2) {
				intersections.add(l);
			}
		}
		
		for(int k = 0; k < (hozWidth - 40)/10; k ++)
		{
		s = new Sidewalk( hozX + 440 + 10*k, hozY + 130 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "7_" + k ); 
		sidewalks.add(s);
		sidewalkStrip7.add(s);
		}
		
		for(int k = 0 ; k <(hozWidth-40)/10 ; k ++)
		{
		s = new Sidewalk( hozX + 440 + 10*k, hozY + 80 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "8_" + k ); 
		sidewalks.add(s);
		sidewalkStrip8.add(s);
		}
		
		
		//TOP AND BOTTOM OF HORIZONTAL ROADS
		for(int k = 0; k < (hozWidth-40)/10  ; k++)
		{
		s = new Sidewalk( hozX + 440 + 10*k, hozY + 250 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "9_" + k ); 
		sidewalks.add(s);
		sidewalkStrip9.add(s);
		}
		
		for(int k = 0 ; k <(hozWidth+70)/10; k ++)
		{
		s = new Sidewalk( hozX + 120 + 10*k, hozY + 250 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "10_" + k ); 
		sidewalks.add(s);
		sidewalkStrip10.add(s);
		}
		
		for(int k = 0; k < (hozWidth+70)/10 ; k ++)
		{
		s = new Sidewalk( hozX + 110 + 10*k, hozY + 20 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "11_" + k ); 
		sidewalks.add(s);
		sidewalkStrip11.add(s);
		}
		
		for(int k = 0; k < (hozWidth-50)/10; k++)
		{
		s = new Sidewalk( hozX + 450 + 10*k, hozY + 20 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "12_" + k ); 
		sidewalks.add(s);
		sidewalkStrip12.add(s);
		}
		
		
		
		//FAR RIGHT VERTICAL
		for(int k = 0 ; k < (hozWidth + 120)/10;k++)
		{
		s = new Sidewalk( hozX + 650, hozY - 20 + 10*k , sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "13_" + k ); 
		sidewalks.add(s);
		sidewalkStrip13.add(s);
		}
		
		//SMALL INNER BLOCKS - TOP
		for(int k = 0 ; k < (60)/10;k++)
		{
		s = new Sidewalk( hozX + 600, hozY + 20 + 10*k , sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "14_" + k ); 
		sidewalks.add(s);
		sidewalkStrip14.add(s);
		}
		
		for(int k = 0 ; k < (60)/10;k++)
		{
		s = new Sidewalk( hozX + 440, hozY + 20 + 10*k , sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "15_" + k ); 
		sidewalks.add(s);
		sidewalkStrip15.add(s);
		}
		
		for(int k = 0 ; k < (60)/10;k++)
		{
		s = new Sidewalk( hozX + 390, hozY + 20 + 10*k , sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "16_" + k ); 
		sidewalks.add(s);	
		sidewalkStrip16.add(s);
		}
		
		for(int k = 0 ; k < (240)/10;k++)
		{
		s = new Sidewalk( hozX + 110, hozY + 20 + 10*k, sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "17_" + k ); 
		sidewalks.add(s);
		sidewalkStrip17.add(s);
		}
		
		
		//FAR LEFT VERTICAL
		for(int k = 0 ; k < (110)/10;k++)
		{
		s = new Sidewalk( hozX + 60, hozY - 30 + 10*k, sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "18_" + k ); 
		sidewalks.add(s);
		sidewalkStrip18.add(s);
		}
		
		for(int k = 0 ; k < (180)/10;k++)
		{
		s = new Sidewalk( hozX + 60, hozY + 130 + 10*k , sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "19_" + k ); 
		sidewalks.add(s);	
		sidewalkStrip19.add(s);
		}
		
		
		//SMALL INNER BLOCKS - BOTTOM
		for(int k = 0 ; k < (110)/10;k++)
		{
		s = new Sidewalk( hozX + 600, hozY + 140 + 10*k , sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "20_" + k ); 
		sidewalks.add(s);
		sidewalkStrip20.add(s);
		}
		
		for(int k = 0 ; k < (110)/10;k++)
		{
		s = new Sidewalk( hozX + 440, hozY + 140 + 10*k , sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "21_" + k ); 
		sidewalks.add(s);
		sidewalkStrip21.add(s);
		}
		
		for(int k = 0 ; k < (110)/10;k++)
		{
		s = new Sidewalk( hozX + 390, hozY + 140 + 10*k, sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "22_" + k ); 
		sidewalks.add(s);
		sidewalkStrip22.add(s);
		}
		
		
		
		

		
		//Middle
		for(int k = 0 ; k < (hozWidth + 70)/10;k++)
		{
		s = new Sidewalk( hozX + 120 + 10*k, hozY + 130 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "23_" + k ); 
		sidewalks.add(s);	
		sidewalkStrip23.add(s);
		}
		
		
		for(int k = 0 ; k <(hozWidth+90)/20;k++)
		{
		l = new Lane( hozX + 110 + 20*k, hozY + 90, laneWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "7_" + k );
		lanes.add(l);
		road7.add(l);
		if(k == 1) {
			intersections.add(l);
		}
		}
		for(int k = 0; k <(hozWidth+90)/20;k++)
		{
			l = new Lane( hozX + 110 + 20*k, hozY + 110, laneWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "8_" + k );
			lanes.add(l);
			road8.add(l);
			if(k == 13) {
				intersections.add(l);
			}
		}
		
		for(int k = 0 ; k < (hozWidth + 70)/10;k++)
		{
		s = new Sidewalk( hozX + 120 + 10 *k, hozY + 80 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "24_" + k ); 
		sidewalks.add(s);
		sidewalkStrip24.add(s);
		}
		
		
		for(int k =0; k <(hozWidth)/20;k++)
		{
		l = new Lane( hozX + 420 + 20*k, hozY + 90, laneWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "9_" + k );
		lanes.add(l);
		road9.add(l);
		if(k == 2) {
			intersections.add(l);
		}
		}
		
		for(int k=0; k < hozWidth/20;k++)
		{
			l = new Lane( hozX + 420 + 20*k, hozY + 110, laneWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "10_" + k );
			lanes.add(l);
			road10.add(l);
			if(k == 8) {
				intersections.add(l);
			}
		}
		//Bottom
		
		for(int k = 0; k<(hozWidth+300)/20;k++)
		{
			l = new Lane( hozX + 110 + 20*k, hozY + 260, laneWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "11_" + k );
			lanes.add(l);
			road11.add(l);
			if(k == 17) {
				intersections.add(l);
			}
		}
		
		for(int k = 0; k < (hozWidth+300)/20;k++)
		{
		l = new Lane( hozX + 110 + 20*k, hozY + 280, laneWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "12_" + k );
		lanes.add(l);
		road12.add(l);
		if(k == 13){
			intersections.add(l);
		}
		}

		for(int k = 0 ; k < (hozWidth + 40)/10;k++)
		{
		s = new Sidewalk( hozX + 410 + 10*k,  hozY + 300 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "26_" + k ); 
		sidewalks.add(s);
		sidewalkStrip25.add(s);
		}
		
		for(int k = 0 ; k < (hozWidth + 130)/10;k++)
		{
		s = new Sidewalk( hozX + 70 + 10*k, hozY + 300 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "27_" + k ); 
		sidewalks.add(s);	
		sidewalkStrip26.add(s);
		}
		
		
		//Vertical Cross Lanes
		for(int k = 0 ; k < (crossHeight + 90) / 20  ; k++)
		{
			l = new Lane( crossX + 90, crossY  + 20*k, laneWidth, laneWidth , 0, 1, false, Color.DARK_GRAY, Color.black, "13_" + k );
			lanes.add(l);
			road13.add(l);
			if(k == 1) {
				intersections.add(l);
			}
		}
		for(int k = 0 ; k < (crossHeight+70)/20;k++)
		{
			l = new Lane( crossX + 110, crossY + 20 + 20*k, crossWidth, laneWidth, 0, 1, false, Color.DARK_GRAY, Color.black, "14_" + k );
			lanes.add(l);
			road14.add(l);
			if(k == 6){
				intersections.add(l);
			}
		}
		
		for(int k = 0 ; k < (crossHeight+50)/20;k++)
		{
			l = new Lane( crossX + 420, crossY + 20*k, crossWidth, laneWidth, 0, 1, false, Color.DARK_GRAY, Color.black, "15_" + k );
			lanes.add(l);
			road15.add(l);
			if(k == 11) {
				intersections.add(l);
			}
			if(k == 4) {
				intersections.add(l);
			}
		}
		
		for(int k = 0; k < (crossHeight+50)/20;k++)
		{
			l = new Lane( crossX + 440, crossY + 20*k, crossWidth, laneWidth, 0, 1, false, Color.DARK_GRAY, Color.black, "16_" + k );
			lanes.add(l);
			road16.add(l);
			if(k == 7) {
				intersections.add(l);
			}
			if(k == 1) {
				intersections.add(l);
			}
		}
		
		
		for(int k = 0; k < (crossHeight+110)/20;k++)
		{
		l = new Lane( crossX + 650, crossY - 20 + 20*k, crossWidth, laneWidth, 0, 1, false, Color.DARK_GRAY, Color.black, "17_" + k );
		lanes.add(l);
		road17.add(l);
		if(k == 8){
			intersections.add(l);
		}
		}
		
		for(int k = 0; k < (crossHeight+110)/20;k++)
		{
			l = new Lane( crossX + 630, crossY - 20 + 20*k, crossWidth, laneWidth, 0, 1, false, Color.DARK_GRAY, Color.black, "18_" + k );
			lanes.add(l);
			road18.add(l);
			if(k == 4) {
				intersections.add(l);
			}
		}
		
		
		//Residential
		
		for(int k = 0 ; k<(crossHeight - 50)/20;k++)
		{
		l = new Lane( crossX - 210, crossY - 50 + 20*k, crossWidth, laneWidth, 0, 1, false, Color.DARK_GRAY, Color.black, "19_" + k );
		lanes.add(l);
		road19.add(l);
		}
		for(int k = 0; k <(crossHeight - 50)/20;k++)
		{
		l = new Lane( crossX - 230, crossY - 50 + 20*k, crossWidth, laneWidth, 0, 1, false, Color.DARK_GRAY, Color.black, "20_" + k );
		lanes.add(l);
		road20.add(l);
		}
		//Right sidewalks
		
		for(int k = 0 ; k < (120)/10;k++)
		{
		s = new Sidewalk( hozX - 210, hozY - 40 + 10*k, sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "28_" + k ); 
		sidewalks.add(s);
		sidewalkStrip27.add(s);
		}
		
		for(int k = 0 ; k < (130)/10;k++)
		{
		s = new Sidewalk( hozX - 210, hozY + 140 + 10*k, sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "29_" + k ); 
		sidewalks.add(s);	
		sidewalkStrip28.add(s);
		}
		
		//Left sidewalks
		for(int k = 0 ; k < (120)/10;k++)
		{
		s = new Sidewalk( hozX - 260, hozY - 40 + 10*k, sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "30_" + k ); 
		sidewalks.add(s);
		sidewalkStrip29.add(s);
		}
		
		for(int k = 0 ; k < (190)/10;k++)
		{
		s = new Sidewalk( hozX - 260, hozY + 80 + 10*k, sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "31_" + k ); 
		sidewalks.add(s);	
		sidewalkStrip30.add(s);
		}
		
		
		
		for(int k = 0; k < (crossHeight-50)/20; k++)
		{
		l = new Lane( crossX - 210, crossY + 110 + 20*k, crossWidth, laneWidth, 0, 1, false, Color.DARK_GRAY, Color.black, "21_" + k );
		lanes.add(l);
		road21.add(l);
		}
		for(int k = 0 ; k < (crossHeight-50)/20; k++)
		{
		l = new Lane( crossX - 230, crossY + 110 + 20*k, crossWidth, laneWidth, 0, 1, false, Color.DARK_GRAY, Color.black, "22_" + k );
		lanes.add(l);
		road22.add(l);
		}

		
		//Add grid of homes on left
		for ( int i=0; i<=1; i++ ) {
			for ( int j=0; j<3; j++ ) {
				Building home = new Building( i*80+ 70, j*40 + 10, 20, 20, i*60 + 90, j*40 + 10, "Home " + i );
				buildings.add( home );
			}
		}
		
		for ( int i=0; i<=1; i++ ) {
			for ( int j=0; j<3; j++ ) {
				Building home = new Building( i*80+ 70, j*40 + 200, 20, 20, i*60 + 90, j*40 + 18, "Home " + i );
				buildings.add( home );
			}
		}
		
		
		
		//First Section, Top Row
		
		Building restaurant2 = new Building( hozX + 230, hozY + 30, 20, 20, 570, 60, "Restaurant 2" );
		buildings.add(restaurant2);
		Building restaurant7 = new Building( hozX + 230, hozY + 310, 20, 20, 570, 100, "Restaurant 7" );
		buildings.add(restaurant7);
		Building restaurant1 = new Building( hozX + 230, hozY + 140, 20, 20, 570, 200, "Restaurant 1" );
		buildings.add(restaurant1);
	
		Building restaurant3 = new Building( hozX + 460, hozY + 30, 20, 20, 770, 60, "Restaurant 3" );
		buildings.add(restaurant3);
		Building bank = new Building( hozX + 370, hozY + 60, 20, 20, 770, 100, "Bank" );
		buildings.add(bank);
		Building restaurant6 = new Building( hozX + 460, hozY + 140, 20, 20, 770, 150, "Restaurant 6" );
		buildings.add(restaurant6);
		Building market = new Building( hozX + 450, hozY + 190, 20, 20, 770, 200, "Market" );
		buildings.add(market);
		
	
		Building restaurant4 = new Building( hozX + 660, hozY + 60, 20, 20, 990, 100, "Restaurant 4" );
		buildings.add(restaurant4);		
		Building restaurant5 = new Building( hozX + 660, hozY + 160, 20, 20, 990, 200, "Restaurant 5" );
		buildings.add(restaurant5);
		
		BusAgent busAgent = new BusAgent();
		busStops.add(new BusStop(220,180,30,30,220,152, "BusStop1"));
		busStops.add(new BusStop(680,350,30,30,680,322, "BusStop2"));
		busStops.add(new BusStop(880,90,30,30,870,132, "BusStop3"));
		busStops.add(new BusStop(650,90,30,30,660,132, "BusStop4"));
		
		BusGui bg = new BusGui(5, 5, 10, 10, road2, road2.get(0), allRoads, this);
		busAgent.setGui(bg);
		busAgent.startThread();
		bg.msgGoToNextStop(busAgent, busStops.get(busStops.size()-1));
		vehicles.add(bg);
//		
		addMouseListener( this );


		Vehicle vehicle = new Vehicle(5, 5, 10, 10, road2, road2.get(0), allRoads, this,"Car");
		vehicle.setDestination(20,20);
		//vehicles.add(vehicle);
//		//vehicle.setDestination(580, 42);
//		//vehicle.setDestination(800, 42);
//		//vehicle.setDestination(580, 152);
//		//vehicle.setDestination(580,322);
//		//vehicle.setDestination(752,120); //not working
//		vehicle.setDestination(772, 180);
//		vehicles.add(vehicle);


	}
	
	public void paintComponent( Graphics g ) {
		count++;
		if(count % 20 == 0) {
			for(Lane intersection : intersections) {
				//System.out.println(intersection.name);
				//System.out.println("RED LIGHT");
				intersection.redLight();
			}
		}
		if(count % 200 == 0) {
			for(Lane intersection : intersections) {
				//System.out.println("GREEN LIGHT");
				intersection.greenLight();
			}
		}
		Graphics2D g2 = (Graphics2D)g;
		
        g2.setColor(getBackground());
        g2.fillRect(0, 0, 500, 500);

		for ( int i=0; i<sidewalks.size(); i++ ) {
			Sidewalk s = sidewalks.get(i);
			s.draw( g2 );
		}
		for ( int i=0; i<buildings.size(); i++ ) {
			Building b = buildings.get(i);
			g2.fill( b );
		}
		
		for ( int i=0; i<lanes.size(); i++ ) {
			Lane l = lanes.get(i);
			l.draw( g2 );
		}
		for(int i = 0; i <busStops.size(); i++){
			BusStop bs = busStops.get(i);
			g2.setColor(Color.YELLOW);
			g2.fill(bs);
		}
		for(int i=0;i<vehicles.size();i++) {
			Vehicle v = vehicles.get(i);
			v.draw(g2);
		}
		for(int i=0;i<people.size();i++) {
			PersonGui p = people.get(i);
			p.setDestination(1002,80 );
			p.draw(g2);
		}
		
	}
	
	public void removeVehicle(Vehicle v) {
		vehicles.remove(v);
	}
	public void removePerson(PersonGui p) {
		people.remove(p);
	}
	public ArrayList<Building> getBuildings() {
		return buildings;
	}
	
	public void mouseClicked(MouseEvent me) {
		//Check to see which building was clicked
		for ( int i=0; i<buildings.size(); i++ ) {
			Building b = buildings.get(i);
			if ( b.contains( me.getX(), me.getY() ) ) {
				
				
				b.displayBuilding();
			}
		}
		
		
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}
}
