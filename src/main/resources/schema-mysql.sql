CREATE TABLE IF NOT EXISTS `fillin` (
  `quiz_id` int NOT NULL,
  `question_id` int NOT NULL,
  `email` varchar(45) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `age` int DEFAULT '0',
  `sex` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `answer_str` varchar(500) DEFAULT NULL,
  `fillin_date` date DEFAULT NULL,
  PRIMARY KEY (`quiz_id`,`question_id`,`email`)
) ;
