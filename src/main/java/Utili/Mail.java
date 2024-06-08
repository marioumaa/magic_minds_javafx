package Utili;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Mail {
    public static void sendEmail(String recepient,String s) throws Exception
    {
        System.out.println("Prepare");
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.office365.com");
        properties.put("mail.smtp.port", "587");
        System.out.println("Prepare");
        String myAccountEmail="douaaghofrane.hadidi@esprit.tn";
        String password="ta28!@do22";
        Session session=Session.getInstance(properties,new Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(myAccountEmail,password);

            }
        });
        Message message = prepareMessage(session,myAccountEmail,recepient,s);
        Transport.send(message);
        System.out.println("Email envoyee avec succes");




    }


    private static Message prepareMessage(Session session,String myAccountEmail,String recepient,String msg) {
        try {
            Message message=new MimeMessage(session);
            message.setFrom(new InternetAddress (myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress (recepient));
            message.setSubject("New Participation ♥");
            message.setText(msg);
            message.reply(false);
            return message;

        }
        catch(Exception e)
        {
            e.getMessage();
        }
        return null;
    }

}