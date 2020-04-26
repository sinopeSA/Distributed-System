
// Java implementation for a client 
// Save file as Client1.java 
  
import java.io.*; 
import java.net.*; 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner; 
  
// Client class 
public class Client1  
{ 
    public static void main(String[] args) throws IOException  
    { 
        try
        { 
            Scanner Scan = new Scanner(System.in); 
              
            // getting localhost ip 
            InetAddress ip = InetAddress.getByName("localhost"); 
      
            // establish the connection with server port 5056 
            Socket Skt = new Socket(ip, 5058); 
      
            // obtaining input and out streams 
            DataInputStream DataInput = new DataInputStream(Skt.getInputStream()); 
            DataOutputStream DataOutput = new DataOutputStream(Skt.getOutputStream()); 
            
            // the following loop performs the exchange of 
            // information between client and client handler 
            while (true)  
            {   
                System.out.println(DataInput.readUTF()); 
                String tosend = Scan.nextLine(); 
                DataOutput.writeUTF(tosend); 
                // If client sends exit,close this connection  
                // and then break from the while loop 
                if(tosend.equals("1")) 
                {   System.out.println(DataInput.readUTF()); 
                    tosend = Scan.nextLine(); 
                    DataOutput.writeUTF(tosend);
                } 
                if(tosend.equals("2")) 
                {   System.out.println(DataInput.readUTF()); 
                    tosend = Scan.nextLine(); 
                    DataOutput.writeUTF(tosend);
                    System.out.println(DataInput.readUTF());
                    tosend = Scan.nextLine(); 
                    DataOutput.writeUTF(tosend);
                    System.out.println(DataInput.readUTF());
                    tosend = Scan.nextLine(); 
                    DataOutput.writeUTF(tosend);
                } 
                if(tosend.equals("3")) 
                {   System.out.println(DataInput.readUTF()); 
                    tosend = Scan.nextLine(); 
                    DataOutput.writeUTF(tosend);
                    System.out.println(DataInput.readUTF());
                    tosend = Scan.nextLine(); 
                    DataOutput.writeUTF(tosend);
                    System.out.println(DataInput.readUTF());
                    tosend = Scan.nextLine(); 
                    DataOutput.writeUTF(tosend);
                    System.out.println(DataInput.readUTF());
                    tosend = Scan.nextLine();
                    DataOutput.writeUTF(tosend);
                } 
                if(tosend.equals("4")) 
                { 
                    System.out.println("Closing this connection : " + Skt); 
                    Skt.close(); 
                    System.out.println("Connection closed"); 
                    break; 
                } 
                  
                // printing date or time as requested by client 
                String received = DataInput.readUTF(); 
                System.out.println(received); 
            } 
              
            // closing resources 
            Scan.close(); 
            DataInput.close(); 
            DataOutput.close(); 
        }
        
        catch(Exception e){ 
            e.printStackTrace(); 
        } 
    } 
} 
