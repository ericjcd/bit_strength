package com.bit.strength.stress.network.RMI;


import jpcap.*;
 
import jpcap.packet.*;
 
import java.io.*;
 
 
 
public class ArpRemote implements PacketReceiver{
 
       public void receivePacket(Packet p){
 
              System.out.println("***********�������ݰ�*******************");  
 
              //System.out.println(p.toString());
 
              System.out.println("����:\t"+p.caplen);
 
              System.out.println("����ͷ��\t");
 
              for(int i=0;i<p.header.length;i++){
 
                     System.out.print(Byte.toString(p.header[i]));      
 
              }
 
              System.out.println();
 
              System.out.println("IP������:");
 
              System.out.println("ԴIP:\t"+ ((IPPacket)p).src_ip.toString() );
 
              System.out.println("Ŀ��IP��\t"+ ((IPPacket)p).dst_ip.toString() );
 
              System.out.println("***********�������ݰ�*******************");  
 
       }
 
      
 
       public static void main(String[] args) throws IOException{
 
              NetworkInterface[] devices = jpcap.JpcapCaptor.getDeviceList();
 
              for(int i=0;i<devices.length;i++){
 
                     System.out.println("DEVICES "+i+":");
 
                     System.out.println("name:\t"+devices[i].name);
 
                     System.out.println("description:\t"+devices[i].description);
 
                     System.out.println("datalink_name:\t"+devices[i].datalink_name);
 
                            System.out.println("datalink_description:\t"+devices[i].datalink_description);
 
                     System.out.println("mac_address:\t");
 
                     for(int j=0;j<devices[i].mac_address.length;j++){
 
                        System.out.print(Integer.toHexString(devices[i].mac_address[j]&0xff) + ":");
 
                     }
 
                     System.out.println();
 
                     System.out.println("NetworkInterfaceAddress:\t");
 
                     for(int j=0;j<devices[i].addresses.length;j++){
 
                            
 
                       System.out.println("address:\t"+devices[i].addresses[j].address);
 
                          
                        System.out.println("broadcast:\t"+devices[i].addresses[j].broadcast);
 
                             
                     System.out.println("destination:\t"+devices[i].addresses[j].destination);
 
                            System.out.println("subnet:\t"+devices[i].addresses[j].subnet);
 
                     }
 
              }
 
              System.out.println("***********************************");
 
             
 
              JpcapCaptor cap = jpcap.JpcapCaptor.openDevice(devices[1],1028,true,10000);
 
              cap.loopPacket(-1,new ArpRemote());
 
       }    
 
}