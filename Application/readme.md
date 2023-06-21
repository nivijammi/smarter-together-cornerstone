1 StudyGroup
   PartKey                                                  
   GroupId   GroupName        discussionTopic     creationDate    active  
   1000      StudyGroup1      Algebra1  
   1000      StudyGroup1      Algebra1
   1000      StudyGroup1      Algebra1   
   1000      StudyGroup1      Algebra1

2 User
   userId  userName  email       password
   77665   nivi      niv@kenzie  1234 

3 StudyGroupMember   
   PartKey   RangeKey                                                    
   GroupId   UserID       GroupName        discussionTopic     creationDate    active  
   1000      101          StudyGroup1      Algebra1  
   1000      102          StudyGroup1      Algebra1
   1000      103          StudyGroup1      Algebra1   
   1000      104          StudyGroup1      Algebra1

   1001      101          StudyGroup2      DataStructures  
   1001      102          StudyGroup2      DataStructures
   1001      103          StudyGroup2      DataStructures   
   1001      104          StudyGroup2      DataStructures

   1002      101          StudyGroup3      ProgrammingBasics  
   1002      102          StudyGroup3      ProgrammingBasics
   1002      103          StudyGroup3      ProgrammingBasics   
   1002      104          StudyGroup3      ProgrammingBasics

2 GSI: Topic-Group ??? 
   <   PrimaryKey   >
   PartKey   RangeKey
   topicId    GroupId 