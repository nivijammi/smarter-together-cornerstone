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


++++++++++++++++++++++++++++++++++++++++++++
Login


  Client                         Server
WebBrowser                        API 

             user, pwd & email
HTML/JS  --------------------->   /login 
               Over SSL 
            [Secured/Encrypted Channel] 

#### User Registration fLow
- The api receives the Email/Pwd  [Other profile attributes - FirstName, LastName, Age...]
- Does Email/HashedPwd combination exists in the system?
  - Yes: You cannot register another user with the same email/username
  - No: 
    - Create the hash of the pwd and 
    - store it in the db.
         Email/Username [HashKey] HashedPwd attribute

#### User Login Flow
- Does User  profile exists in the system?
   - Check if username/email exists in the db?
      - Yes - User exits in db 
        - Get the Email/HashedPwd from the db
        - Hash the user provided pwd. 
        - Does user provided hashed pwd match the stored hashed pwd for the username/email
          - Yes: login successful
          - No: Login failure
      - No [User does not exist in db]
        - Send the appropriate response back
        
