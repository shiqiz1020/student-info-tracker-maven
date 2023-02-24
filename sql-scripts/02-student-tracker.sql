CREATE DATABASE  IF NOT EXISTS `web_student_tracker`;
USE `web_student_tracker`;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
); 

--
-- Dumping data for table `student`
--

INSERT INTO `student` VALUES (1,'Mary','A','maryA@gmail.com'),(2,'John','Doe','johnd@gmail.com'),(3,'Ajay','R','ajayr@gmail.com'),(4,'Bill','Whatever','billw@gmail.com'),(5,'Maxwell','Dixon','maxd@icloud.com');