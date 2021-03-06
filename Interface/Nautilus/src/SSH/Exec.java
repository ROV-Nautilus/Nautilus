package SSH;

/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/**
 * This program will demonstrate remote exec.
 *   $ CLASSPATH=.:../build javac Exec.java 
 *   $ CLASSPATH=.:../build java Exec
 * You will be asked username, hostname, displayname, passwd and command.
 * If everything works fine, given command will be invoked 
 * on the remote side and outputs will be printed out.
 *
 */
import com.jcraft.jsch.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

public class Exec implements Runnable{
	
	public String Commande = " ";
	public String retour;
	public Channel channel;
	public InputStream in;
	public Session session;
	
	public Exec(String Commande) {
		this.Commande=Commande;
	}
	/** Explication ligne par ligne effectu� dans la rapport final*/
	public Exec() {
		try{
			JSch jsch=new JSch();
      		this.session=jsch.getSession("pi", "169.254.14.03", 22);
      UserInfo ui=new MyUserInfo();// username and password will be given via UserInfo interface.
      this.session.setUserInfo(ui);
      this.session.connect();
      
      this.channel = this.session.openChannel("exec");
      ((ChannelExec)this.channel).setCommand(this.Commande);
      this.channel.setInputStream(null);
      ((ChannelExec)this.channel).setErrStream(System.err);
      this.in=this.channel.getInputStream();
      
      this.channel.connect();
      
      byte[] tmp=new byte[1024];
      while(true){
        while(this.in.available()>0){
          int i=this.in.read(tmp, 0, 1024);
          if(i<0)break;
          System.out.print(new String(tmp, 0, i));
          //this.retour=new String(tmp, 0, i);
        }
        if(channel.isClosed()){
          if(this.in.available()>0) continue; 
          System.out.println("exit-status: "+channel.getExitStatus());
          break;
        }
      }
		}
    catch(Exception e){
      System.out.println(e);
    }
}
	public void setCommande(String Commande) {
		this.Commande=Commande;
	}
	
	public void Commander(String Commande) {
		try{
		this.channel = this.session.openChannel("exec");
		((ChannelExec)this.channel).setCommand(Commande);
		this.channel.setInputStream(null);
	    ((ChannelExec)this.channel).setErrStream(System.err);
	    this.in=this.channel.getInputStream();
	    this.channel.connect();
	    
		byte[] tmp=new byte[1024];
	      while(true){
	        while(this.in.available()>0){
	          int i=this.in.read(tmp, 0, 1024);
	          if(i<0)break;
	          //System.out.print(new String(tmp, 0, i));
	          this.retour=new String(tmp, 0, i);
	        }
	        if(channel.isClosed()){
	          if(this.in.available()>0) continue; 
	          //System.out.println("exit-status: "+channel.getExitStatus());
	          break;
	        }
	      }
		}
	      catch(Exception e){
	          System.out.println(e);
	        }
	}
	
  public static class MyUserInfo implements UserInfo, UIKeyboardInteractive{
    public String getPassword(){ return passwd; }
    public boolean promptYesNo(String str){return true;}
  
    String passwd="raspberry";
    JTextField passwordField=(JTextField)new JPasswordField(20);

    public String getPassphrase(){ return null; }
    public boolean promptPassphrase(String message){ return true; }
    public boolean promptPassword(String message){
        return true;
    }
    public void showMessage(String message){
      JOptionPane.showMessageDialog(null, message);
    }
    final GridBagConstraints gbc = 
      new GridBagConstraints(0,0,1,1,1,1,
                             GridBagConstraints.NORTHWEST,
                             GridBagConstraints.NONE,
                             new Insets(0,0,0,0),0,0);
    private Container panel;
    public String[] promptKeyboardInteractive(String destination,
                                              String name,
                                              String instruction,
                                              String[] prompt,
                                              boolean[] echo){
      panel = new JPanel();
      panel.setLayout(new GridBagLayout());

      gbc.weightx = 1.0;
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      gbc.gridx = 0;
      panel.add(new JLabel(instruction), gbc);
      gbc.gridy++;

      gbc.gridwidth = GridBagConstraints.RELATIVE;

      JTextField[] texts=new JTextField[prompt.length];
      for(int i=0; i<prompt.length; i++){
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.weightx = 1;
        panel.add(new JLabel(prompt[i]),gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1;
        if(echo[i]){
          texts[i]=new JTextField(20);
        }
        else{
          texts[i]=new JPasswordField(20);
        }
        panel.add(texts[i], gbc);
        gbc.gridy++;
      }

      if(JOptionPane.showConfirmDialog(null, panel, 
                                       destination+": "+name,
                                       JOptionPane.OK_CANCEL_OPTION,
                                       JOptionPane.QUESTION_MESSAGE)
         ==JOptionPane.OK_OPTION){
        String[] response=new String[prompt.length];
        for(int i=0; i<prompt.length; i++){
          response[i]=texts[i].getText();
        }
	return response;
      }
      else{
        return null;  // cancel
      }
    }
  }
  
  public void run() {
	    Commander(this.Commande);
	    System.out.println("ok : "+this.Commande);
	  }
  
}