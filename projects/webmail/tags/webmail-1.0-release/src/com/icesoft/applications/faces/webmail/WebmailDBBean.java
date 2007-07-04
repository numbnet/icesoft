/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail;

import com.icesoft.applications.faces.webmail.util.db.DBBean;
import com.icesoft.applications.faces.webmail.util.db.HibernateUtil;
import com.icesoft.applications.faces.webmail.login.User;
import com.icesoft.applications.faces.webmail.mail.MailAccount;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class WebmailDBBean extends DBBean {

    private String inputFile;

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public Configuration createSchema(Configuration cfg) {

        Configuration config = cfg.configure();
        new SchemaExport(config)
                .setImportFile(inputFile)
                .create(true, true);

        return config;

    }

    public void load() {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        session.save(new User("anon1", "temp1"));
        session.save(new User("anon2", "temp2"));
        session.save(new User("anon3", "temp3"));
        session.save(new User("anon4", "temp4"));
        session.save(new User("anon5", "temp5"));
        session.save(new User("anon6", "temp6"));
        session.save(new User("anon7", "temp7"));
        session.save(new User("anon8", "temp8"));

        MailAccount mailAccount = new MailAccount();
        mailAccount.setUserName("anon1");
        mailAccount.setProtocol("imap");
        mailAccount.setHost("demo.icesoft.com");
        mailAccount.setIncomingHost("deomo.icesoft.com");
        mailAccount.setIncomingPort(143);
        mailAccount.setIncomingSsl(false);
        mailAccount.setOutgoingHost("www.icesoft.ca");
        mailAccount.setOutgoingPort(25);
        mailAccount.setOutgoingSsl(false);
        mailAccount.setOutgoingVerification(false);
        mailAccount.setMailUsername("icetest1");
        mailAccount.setPassword("ic3t35t3R");
        session.save(mailAccount);

        session.getTransaction().commit();
    }

}