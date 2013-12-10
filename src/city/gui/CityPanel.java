package city.gui;
import javax.swing.*;

import people.PeopleAgent;

import transportation.BusAgent;
import transportation.BusPassengerRole;
import transportation.BusStop;
import transportation.CarAgent;
import transportation.CarGui;
import transportation.CarPassengerRole;
import transportation.gui.BusGui;
import transportation.gui.BusStopGui;
import transportation.gui.InsideBusGui;
import transportation.interfaces.Bus;

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
	public ArrayList<VehicleGui> vehicles;
	public ArrayList<PersonGui> people;
	public List<BusStop> busStops;
	public List<BusGui> buses;
	ArrayList<Lane> road1,road2,road3,road4,road5,road6,road7,road8,road9,road10,road11,road12,road13,road14,road15,road16,road17,road18,road19,road20,road21,road22;
	ArrayList<ArrayList<Lane>> allRoads;
	ArrayList<Sidewalk> sidewalkStrip1,sidewalkStrip2,sidewalkStrip3,sidewalkStrip4,sidewalkStrip5,sidewalkStrip6,
	sidewalkStrip7,sidewalkStrip8,sidewalkStrip9,sidewalkStrip10,sidewalkStrip11,sidewalkStrip12,sidewalkStrip13,sidewalkStrip14,sidewalkStrip15,
	sidewalkStrip16,sidewalkStrip17,sidewalkStrip18,sidewalkStrip19,sidewalkStrip20,sidewalkStrip21,sidewalkStrip22,sidewalkStrip23,sidewalkStrip24,
	sidewalkStrip25,sidewalkStrip26,sidewalkStrip27,sidewalkStrip28,sidewalkStrip29,sidewalkStrip30;
	ArrayList<ArrayList<Sidewalk>> allSidewalks;
	ArrayList<Lane> intersections;
	ArrayList<Sidewalk> crosswalks;
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
	public String time;
	JLabel clock;
	
	
	public ImageIcon background = new ImageIcon("res/background.png");
	
	
	


	public CityPanel(CityGui city) {
		clock = new JLabel(time);
		this.add(clock);
		buildings = new ArrayList<Building>();
		lanes = new ArrayList<Lane>();
		sidewalks = new ArrayList<Sidewalk>();
		vehicles = new ArrayList<VehicleGui>();
		people = new ArrayList<PersonGui>();
		busStops = new ArrayList<BusStop>();
		buses = new ArrayList<BusGui>();
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
		crosswalks = new ArrayList<Sidewalk>();
		
		
		
		
		
		


		//Create grid of lanes
		
		//Center Horizontal Top Lanes
		Sidewalk s;
		for(int k = 0; k < hozWidth/10 ; k++)
		{
		s = new Sidewalk( hozX - 210 + 10*k, hozY + 130 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "0_" + k ); 
		sidewalks.add(s);
		sidewalkStrip1.add(s);
		}
		
		Lane l;
		for(int k = 0 ; k<((hozX - 210) + hozY+140)/20 ; k++)
		{
			l = new Lane( hozX - 210 + 20*k, hozY + 90, laneWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "0_" + k );
			lanes.add( l );
			road1.add(l);
		}
		
		for(int k = 0 ; k<((hozX - 210) + hozY+110)/20 ; k++)
		{
			l = new Lane( hozX - 210 + 20*k, hozY + 110, laneWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "1_" + k);
			lanes.add( l );
			road2.add(l);
			if(k == 13) {
				intersections.add(l); //First intersection
			}
		}
		
		for(int k = 0; k < hozWidth/10; k++)
		{
			s = new Sidewalk( hozX - 210 + 10*k, hozY + 80 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "1_" + k ); 
			sidewalks.add(s);
			sidewalkStrip2.add(s);
		}
		
		for(int k = 0 ; k < (hozWidth - 100)/10;k++)
		{
			s = new Sidewalk( hozX + 10*k, hozY + 130 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "2_" + k ); 
			sidewalks.add(s);
			sidewalkStrip3.add(s);
			if(k==5) {
				crosswalks.add(s);
			}
		}
		
		
		for(int k = 0; k < (hozWidth-140)/10; k ++)
		{
			s = new Sidewalk( hozX + 10*k, hozY + 80 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "3_" + k ); 
			sidewalks.add(s);
			sidewalkStrip4.add(s);
		}
		
		//Beginning of main city
		
		//Top of city
		for(int k = 0 ; k < (hozWidth + 120)/10 ; k ++) {
			s = new Sidewalk( hozX + 70 + 10*k, hozY - 30 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "4_" + k ); 
			sidewalks.add(s);
			sidewalkStrip5.add(s);
		}
		
		for(int k = 0 ; k<(hozWidth +140 )/20  ; k++) {
			l = new Lane( hozX + 70 + 20*k, hozY, laneWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "2_" + k );
			lanes.add( l );
			road3.add(l);
			if(k == 15) {
				intersections.add(l);
			}
			
		}
		
		for(int k = 0 ; k<(hozWidth + 150)/20 ; k++) {
			l = new Lane( hozX + 70 + 20*k, hozY - 20, laneWidth , hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "3_" + k );
			lanes.add( l );
			road4.add(l);
		
		}

		for(int k = 0 ; k < (hozWidth+50)/10 ; k ++)
		{
		s = new Sidewalk( hozX + 400 + 10*k, hozY - 30 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "5_" + k ); 
		sidewalks.add(s);
		sidewalkStrip6.add(s);
		}
		
		for(int k = 0; k<((hozWidth - 10)/20); k++)
		{
		l = new Lane( hozX + 420 + 20*k, hozY, laneWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "4_" + k );
		lanes.add( l );
		road5.add(l);
		}
		
		for(int k = 0; k <(hozWidth + 20)/20;k++)
		{
			l = new Lane( hozX + 420 + 20*k, hozY-20, laneWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "5_" + k );
			lanes.add( l );
			road6.add(l);
			if(k == 2) {
				intersections.add(l);
			}
		}
		
		for(int k = 0; k < (hozWidth )/10; k ++)
		{
			s = new Sidewalk( hozX + 440 + 10*k, hozY + 130 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "6_" + k ); 
			sidewalks.add(s);
			sidewalkStrip7.add(s);
			if(k == 16) {
				crosswalks.add(s);
			}
		}
		
		for(int k = 0 ; k <(hozWidth)/10 ; k ++)
		{
		s = new Sidewalk( hozX + 440 + 10*k, hozY + 80 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "7_" + k ); 
		sidewalks.add(s);
		sidewalkStrip8.add(s);
		}
		
		
		//TOP AND BOTTOM OF HORIZONTAL ROADS
		for(int k = 0; k < (hozWidth-40)/10  ; k++)
		{
		s = new Sidewalk( hozX + 440 + 10*k, hozY + 250 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "8_" + k ); 
		sidewalks.add(s);
		sidewalkStrip9.add(s);
		}
		
		for(int k = 0 ; k <(hozWidth+160)/10; k ++)
		{
		s = new Sidewalk( hozX + 70 + 10*k, hozY + 250 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "9_" + k ); 
		sidewalks.add(s);
		sidewalkStrip10.add(s);
		}
		
		for(int k = 0; k < (hozWidth+160)/10 ; k ++)
		{
		s = new Sidewalk( hozX + 70 + 10*k, hozY + 20 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "10_" + k ); 
		sidewalks.add(s);
		sidewalkStrip11.add(s);
		if(k==32) {
			crosswalks.add(s);
		}
		}
		
		for(int k = 0; k < (hozWidth-10)/10; k++)
		{
			s = new Sidewalk( hozX + 450 + 10*k, hozY + 20 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "11_" + k ); 
			sidewalks.add(s);
			sidewalkStrip12.add(s);
			if(k == 15) {
				crosswalks.add(s);
			}
		}
		
		
		
		//FAR RIGHT VERTICAL
		for(int k = 0 ; k < (hozWidth + 120)/10;k++)
		{
		s = new Sidewalk( hozX + 650, hozY - 20 + 10*k , sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "12_" + k ); 
		sidewalks.add(s);
		sidewalkStrip13.add(s);
		}
		
		//SMALL INNER BLOCKS - TOP
		for(int k = 0 ; k < (150)/10;k++)
		{
		s = new Sidewalk( hozX + 600, hozY - 20 + 10*k , sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "13_" + k ); 
		sidewalks.add(s);
		sidewalkStrip14.add(s);
		}
		
		for(int k = 0 ; k < (150)/10;k++)
		{
		s = new Sidewalk( hozX + 440, hozY - 20 + 10*k , sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "14_" + k ); 
		sidewalks.add(s);
		sidewalkStrip15.add(s);
		}
		
		for(int k = 0 ; k < (150)/10;k++)
		{
		s = new Sidewalk( hozX + 390, hozY - 20 + 10*k , sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "15_" + k ); 
		sidewalks.add(s);	
		sidewalkStrip16.add(s);
		}
		
		for(int k = 0 ; k < (240+80)/10;k++)
		{
		s = new Sidewalk( hozX + 110, hozY - 20 + 10*k, sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "16_" + k ); 
		sidewalks.add(s);
		sidewalkStrip17.add(s);
		if(k==15) {
			crosswalks.add(s);
		}
		}
		
		
		//FAR LEFT VERTICAL
		for(int k = 0 ; k < (160)/10;k++)
		{
		s = new Sidewalk( hozX + 60, hozY - 30 + 10*k, sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "17_" + k ); 
		sidewalks.add(s);
		sidewalkStrip18.add(s);
		}
		
		for(int k = 0 ; k < (180)/10;k++)
		{
		s = new Sidewalk( hozX + 60, hozY + 130 + 10*k , sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "18_" + k ); 
		sidewalks.add(s);	
		sidewalkStrip19.add(s);
		}
		
		
		//SMALL INNER BLOCKS - BOTTOM
		for(int k = 0 ; k < (160)/10;k++)
		{
		s = new Sidewalk( hozX + 600, hozY + 140 + 10*k , sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "19_" + k ); 
		sidewalks.add(s);
		sidewalkStrip20.add(s);
		}
		
		for(int k = 0 ; k < (160)/10;k++)
		{
		s = new Sidewalk( hozX + 440, hozY + 140 + 10*k , sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "20_" + k ); 
		sidewalks.add(s);
		sidewalkStrip21.add(s);
		}
		
		for(int k = 0 ; k < (160)/10;k++)
		{
		s = new Sidewalk( hozX + 390, hozY + 140 + 10*k, sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "21_" + k ); 
		sidewalks.add(s);
		sidewalkStrip22.add(s);
		}
		
		
		
		

		
		//Middle
		for(int k = 0 ; k < (hozWidth + 160)/10;k++)
		{
		s = new Sidewalk( hozX + 70 + 10*k, hozY + 130 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "22_" + k ); 
		sidewalks.add(s);	
		sidewalkStrip23.add(s);
		if(k==32) {
			crosswalks.add(s);
		}
		}
		
		
		for(int k = 0 ; k <(hozWidth+120)/20;k++)
		{
		l = new Lane( hozX + 110 + 20*k, hozY + 90, laneWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "6_" + k );
		lanes.add(l);
		road7.add(l);
		if(k == 1) {
			intersections.add(l);
		}
		}
		for(int k = 0; k <(hozWidth+90)/20;k++)
		{
			l = new Lane( hozX + 110 + 20*k, hozY + 110, laneWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "7_" + k );
			lanes.add(l);
			road8.add(l);
			if(k == 13) {
				intersections.add(l);
			}
		}
		
		for(int k = 0 ; k < (hozWidth + 160)/10;k++)
		{
			s = new Sidewalk( hozX + 70 + 10 *k, hozY + 80 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "23_" + k ); 
			sidewalks.add(s);
			sidewalkStrip24.add(s);
			if(k == 4) {
				crosswalks.add(s);
			}
		}
		
		
		for(int k =0; k <(hozWidth)/20;k++)
		{
		l = new Lane( hozX + 420 + 20*k, hozY + 90, laneWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "8_" + k );
		lanes.add(l);
		road9.add(l);
		if(k == 2) {
			intersections.add(l);
		}
		}
		
		for(int k=0; k < hozWidth/20;k++)
		{
			l = new Lane( hozX + 420 + 20*k, hozY + 110, laneWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "9_" + k );
			lanes.add(l);
			road10.add(l);
			if(k == 8) {
				intersections.add(l);
			}
		}
		//Bottom
		
		for(int k = 0; k<(hozWidth+300)/20;k++)
		{
			l = new Lane( hozX + 110 + 20*k, hozY + 260, laneWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "10_" + k );
			lanes.add(l);
			road11.add(l);
			if(k == 17) {
				intersections.add(l);
			}
		}
		
		for(int k = 0; k < (hozWidth+340)/20;k++)
		{
		l = new Lane( hozX + 70 + 20*k, hozY + 280, laneWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black, "11_" + k );
		lanes.add(l);
		road12.add(l);
		if(k == 13){
			intersections.add(l);
		}
		}

		for(int k = 0 ; k < (hozWidth + 40)/10;k++)
		{
			s = new Sidewalk( hozX + 410 + 10*k,  hozY + 300 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "24_" + k ); 
			sidewalks.add(s);
			sidewalkStrip25.add(s);
			if(k == 3) {
				crosswalks.add(s);
			}
		}
		
		for(int k = 0 ; k < (hozWidth + 130)/10;k++)
		{
		s = new Sidewalk( hozX + 70 + 10*k, hozY + 300 , sidewalkHeight, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black, "25_" + k ); 
		sidewalks.add(s);	
		sidewalkStrip26.add(s);
		}
		
		
		//Vertical Cross Lanes
		for(int k = 0 ; k < (crossHeight + 90) / 20  ; k++)
		{
			l = new Lane( crossX + 90, crossY  + 20*k, laneWidth, laneWidth , 0, 1, false, Color.DARK_GRAY, Color.black, "12_" + k );
			lanes.add(l);
			road13.add(l);
			if(k == 1) {
				intersections.add(l);
			}
		}
		for(int k = 0 ; k < (crossHeight+70)/20;k++)
		{
			l = new Lane( crossX + 110, crossY + 20 + 20*k, crossWidth, laneWidth, 0, 1, false, Color.DARK_GRAY, Color.black, "13_" + k );
			lanes.add(l);
			road14.add(l);
			if(k == 6){
				intersections.add(l);
			}
		}
		
		for(int k = 0 ; k < (crossHeight+50)/20;k++)
		{
			l = new Lane( crossX + 420, crossY + 20*k, crossWidth, laneWidth, 0, 1, false, Color.DARK_GRAY, Color.black, "14_" + k );
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
			l = new Lane( crossX + 440, crossY + 20*k, crossWidth, laneWidth, 0, 1, false, Color.DARK_GRAY, Color.black, "15_" + k );
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
		l = new Lane( crossX + 650, crossY - 20 + 20*k, crossWidth, laneWidth, 0, 1, false, Color.DARK_GRAY, Color.black, "16_" + k );
		lanes.add(l);
		road17.add(l);
		if(k == 8){
			intersections.add(l);
		}
		}
		
		for(int k = 0; k < (crossHeight+110)/20;k++)
		{
			l = new Lane( crossX + 630, crossY - 20 + 20*k, crossWidth, laneWidth, 0, 1, false, Color.DARK_GRAY, Color.black, "17_" + k );
			lanes.add(l);
			road18.add(l);
			if(k == 4) {
				intersections.add(l);
			}
		}
		
		
		/*
		 * Residential
		 */
		
		//Top Roads
		for(int k = 0 ; k<(crossHeight - 50)/20;k++)
		{
			l = new Lane( crossX - 210, crossY - 50 + 20*k, crossWidth, laneWidth, 0, 1, false, Color.DARK_GRAY, Color.black, "18_" + k );
			lanes.add(l);
			road19.add(l);
		}
		for(int k = 0; k <(crossHeight - 50)/20;k++)
		{
			l = new Lane( crossX - 230, crossY - 50 + 20*k, crossWidth, laneWidth, 0, 1, false, Color.DARK_GRAY, Color.black, "19_" + k );
			lanes.add(l);
			road20.add(l);
		}
		
		//Right sidewalks
		
		for(int k = 0 ; k < (170)/10;k++)
		{
			s = new Sidewalk( hozX - 210, hozY - 40 + 10*k, sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "26_" + k ); 
			sidewalks.add(s);
			sidewalkStrip27.add(s);
		}
		
		for(int k = 0 ; k < (240)/10;k++)
		{
			s = new Sidewalk( hozX - 210, hozY + 140 + 10*k, sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "27_" + k ); 
			sidewalks.add(s);	
			sidewalkStrip28.add(s);
		}
		
		//Left sidewalks
		for(int k = 0 ; k < (120)/10;k++)
		{
			s = new Sidewalk( hozX - 260, hozY - 40 + 10*k, sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "28_" + k ); 
			sidewalks.add(s);
			sidewalkStrip29.add(s);
		}
		
		for(int k = 0 ; k < (300)/10;k++)
		{
			s = new Sidewalk( hozX - 260, hozY + 80 + 10*k, sidewalkHeight, sidewalkHeight, 0, 0.5, false, Color.gray, Color.black, "29_" + k ); 
			sidewalks.add(s);	
			sidewalkStrip30.add(s);
		}
		
		
		//Bottom Roads
		
		for(int k = 0; k < (crossHeight + 60)/19; k++)
		{
			l = new Lane( crossX - 210, crossY + 110 + 20*k, crossWidth, laneWidth, 0, 1, false, Color.DARK_GRAY, Color.black, "20_" + k );
			lanes.add(l);
			road21.add(l);
		}
		for(int k = 0 ; k < (crossHeight + 80)/19; k++)
		{
			l = new Lane( crossX - 230, crossY + 80 + 20*k, crossWidth, laneWidth, 0, 1, false, Color.DARK_GRAY, Color.black, "21_" + k );
			lanes.add(l);
			road22.add(l);
		}

		
		//Add grid of homes on left
		for ( int i=0; i<=1; i++ ) {
			for ( int j=0; j<3; j++ ) {
				Building home;
				if(i == 0)
				{
					home = new Building( i*80+ 70, j*40 + 10, 20, 20, (122 - 30*i), j*40 + 10, "Home " + ((i+1)*(j+1)) );
				}
				else
				{
					home = new Building( i*80+ 70, j*40 + 10, 20, 20, (122 - 20*i), j*40 + 10, "Home " + (j+4) );
				}
				buildings.add( home );
			}
		}
		//122 
		
		for ( int i=0; i<=1; i++ ) {
			for ( int j=0; j<3; j++ ) {
				Building home;
				if(i == 0)
				{
					home = new Building( i*80+ 70, j*40 + 200, 20, 20, (122 - 30*i), j*40 + 210, "Home " + (6+((i+1)*(j+1))) );
					System.out.println("Home " + (6+(i+1)*(j+1)) + ": " + (122 - 30*i) + "," + (j*40+10));
				}
				else
				{
					home = new Building( i*80+ 70, j*40 + 200, 20, 20, (122 - 20*i), j*40 + 210, "Home " + (6+(j+4)) );
					System.out.println("Home " + (6+(j+4)) + ": " + (122 - 20*i) + "," + (j*40+10));
				}
				buildings.add(home);
			}
		}
		
		Building apartment1 = new Building( 150, hozY + 310, 30, 30, 102, 370, "Apartment 1" );
		buildings.add(apartment1);
		Building apartment2 = new Building( 60, hozY + 310, 30, 30, 122, 370, "Apartment 2" );
		buildings.add(apartment2);
		
		
		
		//First Section, Top Row
		
		Building market = new Building( hozX + 230, hozY + 310, 60, 100, 580, 322, "Market" );
		buildings.add(market);
		Building bank = new Building( hozX + 210, hozY + 140, 80, 80, 560, 302, "Bank" );
		buildings.add(bank);
		Building restaurant1 = new Building( hozX + 460, hozY + 30, 40, 40, 830, 42, "Restaurant 1" );
		buildings.add(restaurant1);
		
		Building restaurant2 = new Building( hozX + 210, hozY + 30, 40, 40, 580, 42, "Restaurant 2" );
		buildings.add(restaurant2);
		
		Building restaurant3 = new Building( hozX + 460, hozY + 140, 40, 40, 810, 152, "Restaurant 3" );
		buildings.add(restaurant3);
		
		Building restaurant4 = new Building( hozX + 450, hozY + 190, 40, 40, 772, 240, "Restaurant 4" );
		buildings.add(restaurant4);
		
		Building restaurant5 = new Building( hozX + 370, hozY + 30, 20, 50, 700, 132, "Restaurant 5" );
		buildings.add(restaurant5);
		
//		Building restaurant5 = new Building( hozX + 660, hozY + 60, 20, 20, 990, 100, "Restaurant 5" );
//		buildings.add(restaurant5);
		
//		Building bank2 = new Building( hozX + 370, hozY + 60, 20, 20, 580, 152, "Bank" );
//		buildings.add(bank2);
		Building restaurant6 = new Building( hozX + 660, hozY + 140, 20, 40, 982, 180, "Restaurant 6" );
		buildings.add(restaurant6);
//		Building restaurant7 = new Building( hozX + 450, hozY + 190, 20, 20, 580, 322, "Restaurant 7" );
//		buildings.add(restaurant7);
//		
//	

//		Building restaurant5 = new Building( hozX + 660, hozY + 160, 20, 20, 990, 200, "Restaurant 5" );
//		buildings.add(restaurant5);

//		
		

		
		
		
//		PeopleAgent person = new PeopleAgent("TEST PERSON", 1000.0, false);
//		PersonGui personGui = new PersonGui( 5, 5, 5, 5, this.sidewalkStrip23,this.sidewalkStrip23.get(3),this.allSidewalks, this, person);					
//		personGui.setDestination("Apartment 2");
//		this.people.add(personGui);
		
		
//		BusPassengerRole bpr = new BusPassengerRole();
//		bpr.setPerson(person);
//		bpr.setCurrentBusStop(busStops.get(1));
//		bpr.setDestinationBusStop(busStops.get(0));
//		bpr.msgIsActive();
		
		CarGui cg = new CarGui(5, 5, 10, 10, road2, road2.get(0), allRoads, this);
		CarAgent carAgent = new CarAgent();
		carAgent.setGui(cg);
		carAgent.startThread();
		cg.msgGoToThisPlace(carAgent, "Apartment 2");
		vehicles.add(cg);
//		

		
//		
		addMouseListener( this );


//		//vehicle.setDestination(580, 42);
//		//vehicle.setDestination(800, 42);
//		//vehicle.setDestination(580, 152);
//		//vehicle.setDestination(580,322);
//		//vehicle.setDestination(752,120); //not working
//		vehicle.setDestination(772, 180);
//		vehicles.add(vehicle);


	}
	
	public void paintComponent( Graphics g ) {
		clock.setText(time);
		count++;
		if(count % 1300 == 0) { //Let people walk everywhere
			for(Sidewalk crosswalk : crosswalks) {
				//FIRST CROSSWALK (MIDDLE)
				if(crosswalk.name.equals("2_5")) {
					crosswalk.greenLight();
				}
				if(crosswalk.name.equals("23_4")) {
					crosswalk.greenLight();
				}
				if(crosswalk.name.equals("16_15")) {
					crosswalk.greenLight();
				}
				
				//SECOND INTERSECTION (MIDDLE)
				if(crosswalk.name.equals("22_32")) {
					crosswalk.greenLight();
				}
				
				//THIRD INTERSECTION (MIDDLE)
				if(crosswalk.name.equals("6_16")) {
					crosswalk.greenLight();
				}
				
				
			}
			for(Lane intersection : intersections) {
				//FIRST INTERSECTION (MIDDLE)
				if(intersection.name.equals("12_1")) {
					intersection.redLight();
				}
				if(intersection.name.equals("13_6")) {
					intersection.redLight();
				}
				if(intersection.name.equals("1_12")) {
					intersection.redLight();
				}
				if(intersection.name.equals("6_1")) {
					intersection.redLight();
				}
				
				//SECOND INTERSECTION (MIDDLE)
				if(intersection.name.equals("7_13")) {
					intersection.redLight();
				}
				if(intersection.name.equals("8_2")) {
					intersection.redLight();
				}
				if(intersection.name.equals("15_6")) {
					intersection.redLight();
				}
				if(intersection.name.equals("14_4")) {
					intersection.redLight();
				}
				
				//THIRD INTERSECTION (MIDDLE)
				if(intersection.name.equals("9_8")) {
					intersection.redLight();
				}
				if(intersection.name.equals("16_8")) {
					intersection.redLight();
				}
				if(intersection.name.equals("17_4")) {
					intersection.redLight();
				}
				
				//TOP INTERSECTION
				if(intersection.name.equals("2_15")) {
					intersection.redLight();
				}
				if(intersection.name.equals("5_2")) {
					intersection.redLight();
				}
				
				//BOTTOM INTERSECTION
				if(intersection.name.equals("11_14")) {
					intersection.redLight();
				}
				if(intersection.name.equals("14_11")) {
					intersection.redLight();
				}
				if(intersection.name.equals("10_17")) {
					intersection.redLight();
				}
			}
		}
		if(count % 600 == 0) {
			for(Sidewalk crosswalk : crosswalks) {
				//FIRST CROSSWALK (MIDDLE)
				if(crosswalk.name.equals("2_5")) {
					crosswalk.redLight();
				}
				if(crosswalk.name.equals("23_4")) {
					crosswalk.redLight();
				}
				if(crosswalk.name.equals("16_15")) {
					crosswalk.redLight();
				}
				
				//SECOND INTERSECTION (MIDDLE)
				if(crosswalk.name.equals("22_32")) {
					crosswalk.redLight();
				}
				
				//THIRD INTERSECTION (MIDDLE)
				if(crosswalk.name.equals("6_16")) {
					crosswalk.redLight();
				}
			}
			for(Lane intersection : intersections) {
				//FIRST INTERSECTION (MIDDLE)
				if(intersection.name.equals("12_1")) {
					intersection.redLight();
				}
				if(intersection.name.equals("13_6")) {
					intersection.redLight();
				}
				if(intersection.name.equals("1_12")) {
					intersection.greenLight();
				}
				if(intersection.name.equals("6_1")) {
					intersection.greenLight();
				}
				
				//SECOND INTERSECTION (MIDDLE)
				if(intersection.name.equals("7_13")) {
					intersection.redLight();
				}
				if(intersection.name.equals("8_2")) {
					intersection.redLight();
				}
				if(intersection.name.equals("15_6")) {
					intersection.greenLight();
				}
				if(intersection.name.equals("14_4")) {
					intersection.greenLight();
				}
				
				//THIRD INTERSECTION (MIDDLE)
				if(intersection.name.equals("9_8")) {
					intersection.redLight();
				}
				if(intersection.name.equals("16_8")) {
					intersection.redLight();
				}
				if(intersection.name.equals("17_4")) {
					intersection.greenLight();
				}
				
				//TOP INTERSECTION
				if(intersection.name.equals("2_15")) {
					intersection.redLight();
				}
				if(intersection.name.equals("5_2")) {
					intersection.greenLight();
				}
				
				//BOTTOM INTERSECTION
				if(intersection.name.equals("11_14")) {
					intersection.redLight();
				}
				if(intersection.name.equals("14_11")) {
					intersection.greenLight();
				}
				if(intersection.name.equals("10_17")) {
					intersection.redLight();
				}
			}
		}
		if(count % 1200 == 0) {
			for(Lane intersection : intersections) {
				if(intersection.name.equals("12_1")) {
					intersection.greenLight();
				}
				if(intersection.name.equals("13_6")) {
					intersection.greenLight();
				}
				if(intersection.name.equals("1_12")) {
					intersection.redLight();
				}
				if(intersection.name.equals("6_1")) {
					intersection.redLight();
				}
				
				//SECOND INTERSECTION (MIDDLE)
				if(intersection.name.equals("7_13")) {
					intersection.greenLight();
				}
				if(intersection.name.equals("8_2")) {
					intersection.greenLight();
				}
				if(intersection.name.equals("15_6")) {
					intersection.redLight();
				}
				if(intersection.name.equals("14_4")) {
					intersection.redLight();
				}
				
				//THIRD INTERSECTION (MIDDLE)
				if(intersection.name.equals("9_8")) {
					intersection.greenLight();
				}
				if(intersection.name.equals("16_8")) {
					intersection.greenLight();
				}
				if(intersection.name.equals("17_4")) {
					intersection.redLight();
				}
				
				//TOP INTERSECTION
				if(intersection.name.equals("2_15")) {
					intersection.greenLight();
				}
				if(intersection.name.equals("5_2")) {
					intersection.redLight();
				}
				
				//BOTTOM INTERSECTION
				if(intersection.name.equals("11_14")) {
					intersection.greenLight();
				}
				if(intersection.name.equals("14_11")) {
					intersection.redLight();
				}
				if(intersection.name.equals("10_17")) {
					intersection.greenLight();
				}
			}
			
			for(Sidewalk crosswalk : crosswalks) {
				//FIRST CROSSWALK (MIDDLE)
				if(crosswalk.name.equals("2_5")) {
					crosswalk.redLight();
				}
				if(crosswalk.name.equals("23_4")) {
					crosswalk.redLight();
				}
				if(crosswalk.name.equals("16_15")) {
					crosswalk.greenLight();
				}
				
				//SECOND INTERSECTION (MIDDLE)
				if(crosswalk.name.equals("22_32")) {
					crosswalk.redLight();
				}
				
				//THIRD INTERSECTION (MIDDLE)
				if(crosswalk.name.equals("6_16")) {
					crosswalk.redLight();
				}
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
		
		g.drawImage(background.getImage(), 0, 0, null);
		
		for(int i=0;i<vehicles.size();i++) {
			VehicleGui v = vehicles.get(i);
			v.draw(g2);
		}
		for(int i=0;i<people.size();i++) {
			PersonGui p = people.get(i);
			p.draw(g2);
		}
		
	}
	
	public void removeVehicle(VehicleGui v) {
		v.setDestination(0, 0);
	}
	public void removePerson(PersonGui p) {
		p.setDestination("Black Abyss");
	}
	public ArrayList<Building> getBuildings() {
		return buildings;
	}
	
	public void mouseClicked(MouseEvent me) {
		//Check to see which building was clicked
		System.out.println("Clicked(X,Y): " + "(" + me.getX() + "," + me.getY() + ")");
		for ( int i=0; i<buildings.size(); i++ ) {
			Building b = buildings.get(i);
			if ( b.contains( me.getX(), me.getY() ) ) {
				b.displayBuilding();
			}
		}
		for( int i=0; i<busStops.size(); i++ ) {
			BusStop bs = busStops.get(i);
			if(bs.contains(me.getX(), me.getY() ) ) {
				bs.displayBuilding();
			}
		}
		for( int i=0; i<buses.size(); i++ ) {
			BusGui bus = buses.get(i);
			if(bus.contains(me.getX(), me.getY() ) ) {
				System.out.println("Clicked Bus: " + (i+1));
				bus.displayBuilding();
			}
		}
		for( int i = 0; i<allRoads.size(); i++) {
			ArrayList<Lane> list = allRoads.get(i);
			for(int k = 0; k < list.size(); k++)
			{
				Lane l = list.get(k);
				if(l.rectangle.contains(me.getX(), me.getY() ) ){
					System.out.println("clicked lane: " + (i) + "_" + k);
			}
			
			}
		}
		for( int i = 0; i<allSidewalks.size(); i++) {
			ArrayList<Sidewalk> list = allSidewalks.get(i);
			for(int k = 0; k < list.size(); k++)
			{
				Sidewalk l = list.get(k);
				if(l.rectangle.contains(me.getX(), me.getY() ) ){
					System.out.println("clicked sidewalk: " + (i) + "_" + k);
			}
			
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
	
	public void setTime(int time) {
		this.time = Integer.toString(time);
		
	}
}
