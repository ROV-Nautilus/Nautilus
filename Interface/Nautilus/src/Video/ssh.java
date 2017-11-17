package Video;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.*;


public class ssh{
	    
	public String user = "pi";
	public String password = "raspberry";
	public String host = "169.254.14.03";
	public int port=22;
	public String remoteFile="test.txt";
    
	public ssh(String commande){
		
	    try
	        {
	        JSch jsch = new JSch();
	        Session session = jsch.getSession(user, host, port);
	        session.setPassword(password);
	        session.setConfig("StrictHostKeyChecking", "no");
	        session.connect();
	        
	        Channel channel=session.openChannel("exec");
	        String command = commande;
	        ((ChannelExec)channel).setCommand(command);
	        InputStream in=channel.getInputStream();
	        ((ChannelExec)channel).setErrStream(System.err);
	        channel.connect();
	        channel.disconnect();
            session.disconnect();
            System.out.println("ssh ok");
	        }
	    catch(Exception e){
	    	System.err.print(e);
	    }  
	}
}