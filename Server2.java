
// Java implementation of  Server side 
// It contains two classes : Server and ClientHandler 
// Save file as Server.java 
  
import java.io.*; 
import static java.lang.Integer.parseInt;
import java.text.*; 
import java.util.*; 
import java.net.*; 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
  
// Server class 
public class Server2  
{ 
    public static void main(String[] args) throws IOException  
    { 
        // server is listening on port 5056 
        ServerSocket ServerSkt = new ServerSocket(5058); 
        while (true)  
        { 
            Socket Skt = null; 
            try 
            { 
                // socket object to receive incoming client requests 
                Skt = ServerSkt.accept(); 
                System.out.println("connecting a new client: " + Skt); 
                // obtaining input and out streams 
                DataInputStream DataInput = new DataInputStream(Skt.getInputStream()); 
                DataOutputStream DataOutput = new DataOutputStream(Skt.getOutputStream()); 
                System.out.println("Assigning new thread for new client"); 
                // create a new thread object 
                Thread thread = new ClientHandler(Skt, DataInput, DataOutput); 
                // Invoking the start() method 
                thread.start(); 
            } 
            catch (Exception e){ 
                Skt.close(); 
                e.printStackTrace(); 
            } 
        } 
    } 
} 
  
// ClientHandler class 
class ClientHandler extends Thread  
{ 
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd"); 
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss"); 
    final DataInputStream DataInput; 
    final DataOutputStream DataOutput; 
    final Socket Skt; 
     
    public String checkBalance(String ac_no) {
        String name = "";
       try {    
	        Class.forName("com.mysql.cj.jdbc.Driver");  //load and register the driver
                Connection  con= DriverManager.getConnection ("jdbc:mysql: // localhost:3308/bank_details","root","sinopesa"); 
		Statement st=con.createStatement();  
		ResultSet rs=st.executeQuery("select * from account where account_number="+ac_no);
             while(rs.next()){
             		name += rs.getString(2);
		}
             con.close();
             //close the connection
       }
       catch(Exception ex){
           System.out.println(ex);
            }
    return name;
    }
    
    public String depositBank(String ac_no, String deposit) throws SQLException{
        String name = "";
        int total = parseInt(deposit);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
                Connection  con= DriverManager.getConnection ("jdbc:mysql: // localhost:3308/bank_details","root","sinopesa");
       try {
                con.setAutoCommit(false);
		Statement st=con.createStatement();
		ResultSet rs=st.executeQuery("select * from account where account_number="+ac_no);
             while(rs.next()){
             		name= rs.getString(2);
		}
           
             total += parseInt(name);
             name = String.valueOf(total);
             st.executeUpdate("update account set balance="+name+" where account_number="+ac_no);
             DataOutput.writeUTF("are you sure?");
             String answ = DataInput.readUTF();
             if(!"yes".equals(answ)){
                 con.rollback();
                 name = "no";
             }
             else{
                 con.commit();
             }
             con.setAutoCommit(true);
             con.close();
       }
       catch(Exception ex){
                con.rollback(); 
       }
    return name;
    }
    
    public String moneyTransferBank(String ac_no,String rec_ac_no, String deposit) throws SQLException{
        String name = "";
        int total = -parseInt(deposit);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  //load and register the driver
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
                Connection  con= DriverManager.getConnection ("jdbc:mysql: // localhost:3308/bank_details","root","sinopesa");
       try {    
	       con.setAutoCommit(false);
		Statement st=con.createStatement();
                
		ResultSet rs=st.executeQuery("select * from account where account_number="+ac_no);
             while(rs.next()){
             		name= rs.getString(2);
             }
             
             total += parseInt(name);
             name = String.valueOf(total);
             st.executeUpdate("update account set balance="+name+" where account_number="+ac_no);
             String answ;
             answ=depositBank(rec_ac_no, deposit);
             if(!"no".equals(answ)){    
                 con.commit();
             }
             else{
                 con.rollback();
             }
             con.setAutoCommit(true);
             con.close();
       }
       catch(Exception ex){
           con.rollback();
        
       }
    return name;
    }
    // Constructor 
    public ClientHandler(Socket Skt, DataInputStream DataInput, DataOutputStream DataOutput)  
    { 
        this.Skt = Skt; 
        this.DataInput = DataInput; 
        this.DataOutput = DataOutput; 
    } 
  
    @Override
    public void run()  
    {  
            
        String received; 
        String toreturn; 
        String name = "";
        String acc_no;
        
        
        while (true)  
        { 
            try { 
                 
                 
                // Ask user what he wants 
                 DataOutput.writeUTF("Main Menu\n1.Balance Enquiry\n2.Deposit\n3.Money transfer\n4.Exit\nEnter choice number:");  
                  
                // receive the answer from client 
                received = DataInput.readUTF(); 
                DataOutput.flush();  
                if(received.equals("4")) 
                {  
                    System.out.println("Client " + this.Skt + " sends exit..."); 
                    System.out.println("Closing the connection."); 
                    this.Skt.close(); 
                    System.out.println("Connection closed"); 
                    break; 
                } 
                  
                // creating Date object 
                Date date = new Date(); 
                  
                // write on output stream based on the 
                // answer from the client 
                switch (received) { 
                  
                    case "1" : {
                        DataOutput.writeUTF("Enter Account holder Account Number:");
                        acc_no = DataInput.readUTF(); 
                        name = checkBalance(acc_no);
                        DataOutput.writeUTF(name); 
                        DataOutput.flush();
                        break; 
                    }
                    case "2" :{ 
                        DataOutput.writeUTF("Enter account number:");
                        acc_no = DataInput.readUTF(); 
                        DataOutput.writeUTF(" Enter Deposit Amount:");
                        String deposit = DataInput.readUTF(); 
                        name = depositBank(acc_no,deposit);
                        DataOutput.writeUTF(name);
                        DataOutput.flush();
                        break; 
                    }
                    case "3" :{ 
                        DataOutput.writeUTF("enter your account number" );
                        acc_no = DataInput.readUTF();
                        DataOutput.writeUTF("receivers account number:" );
                        String rec_acc_no = DataInput.readUTF(); 
                        DataOutput.writeUTF("Amount to send:");
                        String deposit = DataInput.readUTF();
                        name = moneyTransferBank(acc_no,rec_acc_no,deposit);
                        DataOutput.writeUTF(name);
                        DataOutput.flush();
                        break; 
                    }
                    default: 
                        DataOutput.writeUTF("Invalid input"); 
                        break; 
                } 
            } catch (IOException e) { 
                e.printStackTrace(); 
            } catch (SQLException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            } 
        } 
          
        try
        { 
            // closing resources 
            this.DataInput.close(); 
            this.DataOutput.close(); 
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
    } 
} 

