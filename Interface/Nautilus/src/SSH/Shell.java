package SSH;

/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/**
 * This program enables you to connect to sshd server and get the shell prompt.
 *   $ CLASSPATH=.:../build javac Shell.java 
 *   $ CLASSPATH=.:../build java Shell
 * You will be asked username, hostname and passwd. 
 * If everything works fine, you will get the shell prompt. Output may
 * be ugly because of lacks of terminal-emulation, but you can issue commands.
 *
 */
import com.jcraft.jsch.*;
import java.awt.*;
import javax.swing.*;

public class Shell{
	
	/** Meme principe que Exec mais avec un shell*/
  public Shell(){
    
    try{
      JSch jsch=new JSch();
      Session session=jsch.getSession("pi", "169.254.14.03", 22);
      session.setPassword("raspberry");

      UserInfo ui = new MyUserInfo(){
        public void showMessage(String message){
          JOptionPane.showMessageDialog(null, message);
        }
        public boolean promptYesNo(String message){ return true;}
      };
      session.setUserInfo(ui);
      session.connect(30000);   // making a connection with timeout.
      Channel channel=session.openChannel("shell");
      channel.setInputStream(System.in);
      channel.setOutputStream(System.out);
      channel.connect(3*1000);
    }
    catch(Exception e){
      System.out.println(e);
    }
  }

  public static abstract class MyUserInfo
                          implements UserInfo, UIKeyboardInteractive{
    public String getPassword(){ return null; }
    public boolean promptYesNo(String str){ return false; }
    public String getPassphrase(){ return null; }
    public boolean promptPassphrase(String message){ return false; }
    public boolean promptPassword(String message){ return false; }
    public void showMessage(String message){ }
    public String[] promptKeyboardInteractive(String destination,
                                              String name,
                                              String instruction,
                                              String[] prompt,
                                              boolean[] echo){
      return null;
    }
  }
}