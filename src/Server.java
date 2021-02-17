import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Server implements Runnable
{  private Socket       socket = null;
    private ServerSocket server = null;
    private Thread       thread = null;
    private DataInputStream  streamIn  =  null;
    private DataOutputStream streamOut = null;
    String user, pass;
    Boolean correct;

    public Server(int port)
    {  try
    {  System.out.println("Binding to port " + port + ", please wait  ...");
        server = new ServerSocket(port);
        System.out.println("Server started: " + server);
        start();
    }
    catch(IOException ioe)
    {  System.out.println(ioe);
    }
    }
    public void run()
    {  while (thread != null)
    {   try
    {  System.out.println("Waiting for a client ...");
        socket = server.accept();
        System.out.println("Client accepted: " + socket);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        timeStamp = "Time: " + timeStamp;
        System.out.println(timeStamp);
        open();
        boolean done = false;
        while (!done)
        {  try
        {  String line = streamIn.readUTF();
            System.out.println("Username: " + line);
            user = line;
            line = streamIn.readUTF();
            System.out.println("Password: " + line);
            pass = line;
            line = streamIn.readUTF();
            done = line.equals("done");
        }
        catch(IOException ioe)
        {  done = true;
        }
        }

        if (checkInfo() == true) {
            System.out.println("Correct Login Info, Accepted");
            System.out.println();
            streamOut.writeUTF("accept");
            streamOut.flush();
            close();
        }
        else{
            System.out.println("Incorrect Login Info, Declined");
            System.out.println();
            streamOut.writeUTF("decline");
            streamOut.flush();
        close();
        }
    }
    catch(IOException ie)
    {  System.out.println("Acceptance Error: " + ie);  }
    }
    }
    public void start()
    {  if (thread == null)
    {  thread = new Thread(this);
        thread.start();
    }
    }
    public void stop()
    {  if (thread != null)
    {  thread.stop();
        thread = null;
    }
    }
    public void open() throws IOException
    {  streamIn = new DataInputStream(new
            BufferedInputStream(socket.getInputStream()));
        streamOut = new DataOutputStream(socket.getOutputStream());
    }
    public void close() throws IOException
    {  if (socket != null)    socket.close();
        if (streamIn != null)  streamIn.close();
    }
    public static void main(String args[])
    {  Server server = null;
            server = new Server(5000);
    }
    public Boolean checkInfo() {
        if ((user.contentEquals("demo")) && (pass.contentEquals("pass"))){
            return true;
        }
        else{
            return false;
        }
    }
}
