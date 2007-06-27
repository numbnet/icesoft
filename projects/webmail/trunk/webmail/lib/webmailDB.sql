## This is the SQL creation script for the webmail application.  It is currently
## divided into two section;
##    1.) the creation of database and child tables
##    2.) the creation of a webmail user and the granting of permissions

## create webmail database if not allready created
CREATE DATABASE IF NOT EXISTS `webmail` ;

USE webmail;

-- create a webmail user
GRANT RELOAD ON * . * TO 'webmail'@'localhost'
IDENTIFIED BY 'webmail'
WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 ;
## add select permission to
GRANT SELECT , INSERT , UPDATE , DELETE ON `webmail` . * TO 'webmail'@'localhost';

DROP TABLE IF EXISTS `addresses`;
CREATE TABLE IF NOT EXISTS `addresses` (
  `id` int(5) NOT NULL auto_increment,
  `contact_id` int(5) NOT NULL default '0',
  `type` varchar(10) NOT NULL default '',
  `country` varchar(20) NOT NULL default '',
  `province` varchar(30) NOT NULL default '',
  `city` varchar(30) NOT NULL default '',
  `street` varchar(50) NOT NULL default '',
  `postcode` varchar(10) NOT NULL default '',
  PRIMARY KEY  (`id`),
  KEY `contact_id` (`contact_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Contact addresses' ;


-- --------------------------------------------------------

--
-- Table structure for table `contacts`
--

DROP TABLE IF EXISTS `contacts`;
CREATE TABLE IF NOT EXISTS `contacts` (
  `id` int(5) NOT NULL auto_increment,
  `discriminator` varchar(20) NOT NULL default 'ContactBean',
  `user_name` varchar(25) NOT NULL default '',
  `initials` varchar(10) NOT NULL default '',
  `last` varchar(50) NOT NULL default '',
  `first` varchar(50) NOT NULL default '',
  `middle` varchar(50) NOT NULL default '',
  `display_name` varchar(50) NOT NULL default '',
  `nick_name` varchar(50) NOT NULL default '',
  `note` varchar(200) NOT NULL default '',
  `primary_email` varchar(50) NOT NULL default '',
  `primary_phone` varchar(20) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Users contacts' ;


-- --------------------------------------------------------

--
-- Table structure for table `emails`
--

DROP TABLE IF EXISTS `emails`;
CREATE TABLE IF NOT EXISTS `emails` (
  `id` int(5) NOT NULL auto_increment,
  `contact_id` int(5) NOT NULL default '0',
  `type` varchar(10) NOT NULL default '',
  `value` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`id`),
  KEY `contact_id` (`contact_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Contact emails' ;

-- --------------------------------------------------------

--
-- Table structure for table `mail_accounts`
--

DROP TABLE IF EXISTS `mail_accounts`;
CREATE TABLE IF NOT EXISTS `mail_accounts` (
  `id` mediumint(9) NOT NULL auto_increment,
  `username` varchar(25) NOT NULL default '',
  `protocol` varchar(10) NOT NULL default '',
  `host` varchar(25) NOT NULL default '',
  `incoming_host` varchar(25) NOT NULL default '',
  `incoming_port` mediumint(9) NOT NULL default '25',
  `incoming_ssl` tinyint(1) NOT NULL default '0',
  `outgoing_verification` tinyint(1) NOT NULL default '0',
  `outgoing_host` varchar(25) NOT NULL default '',
  `outgoing_port` mediumint(9) NOT NULL default '25',
  `outgoing_ssl` tinyint(1) NOT NULL default '0',
  `mail_username` varchar(25) NOT NULL default '',
  `password` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`id`),
  KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Users email accounts' ;

--
-- Dumping data for table `mail_accounts`
--

INSERT INTO `mail_accounts` (`id`, `username`, `protocol`, `host`, `incoming_host`, `incoming_port`, `incoming_ssl`, `outgoing_verification`, `outgoing_host`, `outgoing_port`, `outgoing_ssl`, `mail_username`, `password`)
VALUES
(null, 'admin', 'imap', 'host.setup.this', 'host.setup.this', 143, 0, 0, 'host.setup.this', 25, 0, 'account1', 'us9AXNEUKdwuT0v+rM6OAw==');
-- --------------------------------------------------------

--
-- Table structure for table `phones`
--

DROP TABLE IF EXISTS `phones`;
CREATE TABLE IF NOT EXISTS `phones` (
  `id` int(5) NOT NULL auto_increment,
  `contact_id` int(5) NOT NULL default '0',
  `type` varchar(10) NOT NULL default '',
  `value` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`id`),
  KEY `contact_id` (`contact_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Contact phones';


-- --------------------------------------------------------

--
-- Table structure for table `tasks`
--

DROP TABLE IF EXISTS `tasks`;
CREATE TABLE IF NOT EXISTS `tasks` (
  `id` int(5) NOT NULL auto_increment,
  `username` varchar(25) NOT NULL default '',
  `category` int(2) NOT NULL default '0',
  `name` varchar(50) NOT NULL default '',
  `description` varchar(150) default '',
  `reminder_date` date NOT NULL default '0000-00-00',
  `start_date` date NOT NULL default '0000-00-00',
  `end_date` date NOT NULL default '0000-00-00',
  `priority` int(2) NOT NULL default '0',
  `status` int(2) NOT NULL default '0',
  `percent_complete` double NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Users task events' ;

--
-- Dumping data for table `tasks`
--

INSERT INTO `tasks` (`id`, `username`, `category`, `name`, `description`, `reminder_date`, `start_date`, `end_date`, `priority`, `status`, `percent_complete`)
VALUES
(null, 'admin', 1, 'task 1 test', 'description 1 test', '2006-02-02', '2006-02-02', '2006-02-02', 1, 0, 0.5);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `username` varchar(25) NOT NULL default '',
  `password` varchar(50) NOT NULL default '',
  `first_name` varchar(50) NOT NULL default '',
  `last_name` varchar(50) NOT NULL default '',
  `company` varchar(75) NOT NULL default '',
  PRIMARY KEY  (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='List of valid user for the webmail application ';

--
-- Dumping data for table `users`
-- See com.icesoft.applications.faces.webmail.util.encryption.PasswordHash for details
--

INSERT INTO `users` (`username`, `password`, `first_name`, `last_name`, `company`)
VALUES
('admin', 'mF3AVDeUI18=', "Anonymous", "User 1", "ICEsoft Technologies Inc." );

-- --------------------------------------------------------

--
-- Table structure for table `webpages`
--

DROP TABLE IF EXISTS `webpages`;
CREATE TABLE IF NOT EXISTS `webpages` (
  `id` int(5) NOT NULL auto_increment,
  `contact_id` int(5) NOT NULL default '0',
  `type` varchar(10) NOT NULL default '',
  `value` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`id`),
  KEY `contact_id` (`contact_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Contact webpages' ;


--
-- Constraints for dumped tables
--

--
-- Constraints for table `addresses`
--
ALTER TABLE `addresses`
  ADD CONSTRAINT `addresses_ibfk_1` FOREIGN KEY (`contact_id`) REFERENCES `contacts` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `emails`
--
ALTER TABLE `emails`
  ADD CONSTRAINT `emails_ibfk_1` FOREIGN KEY (`contact_id`) REFERENCES `contacts` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `mail_accounts`
--
ALTER TABLE `mail_accounts`
  ADD CONSTRAINT `mail_accounts_ibfk_1` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `phones`
--
ALTER TABLE `phones`
  ADD CONSTRAINT `phones_ibfk_1` FOREIGN KEY (`contact_id`) REFERENCES `contacts` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `webpages`
--
ALTER TABLE `webpages`
  ADD CONSTRAINT `webpages_ibfk_1` FOREIGN KEY (`contact_id`) REFERENCES `contacts` (`id`) ON DELETE CASCADE;

