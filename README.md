# Care Driving
The system is designed for people who are looking for driving instructors, who are in the process of learning to drive, as well as driving instructors.

The system allows users defined as students a quick and easy solution to a procedure called driving lessons, from the teacher search process to the final stage of the practical test phase.

For users defined as driving instructors, the system serves as a platform for broad advertising and exposure.
### Goals
Make the driving instructor search process accessible and make it an easy, fast and comparable process.
Turn the process of determining driving lessons into an orderly and efficient process.
Establish ways of communication between teacher and student.

### The App
#### Login / Register
![image](https://user-images.githubusercontent.com/44946807/88770307-4ca62080-d186-11ea-8fd5-dc5a846f6a21.png)    ![image](https://user-images.githubusercontent.com/44946807/88771541-0f429280-d188-11ea-9080-eaecce3d3b25.png)
 
##### Registration
When a user wants to register for the system, he must enter the Register screen, where he must fill in his personal details and choose the type of user he is: a student or a teacher.
After selecting the type of user he must fill in details **according to the type of user selected**.
Registration for the system is done using Firebase Authentication, via email and password.

Teacher registraton:

![image](https://user-images.githubusercontent.com/44946807/88771908-9b54ba00-d188-11ea-86fb-01cd82492db6.png)  ![image](https://user-images.githubusercontent.com/44946807/88770977-3d73a280-d187-11ea-8bd1-686f2371933c.png)

Student registraton:

![image](https://user-images.githubusercontent.com/44946807/88770171-136db080-d186-11ea-91ad-8e2c0ce95d05.png)    ![image](https://user-images.githubusercontent.com/44946807/88771348-c8ed3380-d187-11ea-9efd-d65721717249.png)


##### Login
Used for login for registered users (also uses Firebase Authentication).

#### Menu
![image](https://user-images.githubusercontent.com/44946807/88772332-4cf3eb00-d189-11ea-9522-a452c1842c4b.png)   ![image](https://user-images.githubusercontent.com/44946807/88772548-9c3a1b80-d189-11ea-9a8d-a47f17c2e163.png)

All the content of the application is in the menu which varies depending on the type of user.
- Identical screens: Personal area (photo), Home.
- Student: Search Teachers, My Teacher.
- Teacher: Request, My Students.

#### Personal area
![image](https://user-images.githubusercontent.com/44946807/88773083-5af63b80-d18a-11ea-9fbf-101d8859e736.png)

Each user has a personal area where he can see his details stored in the system (by clicking on his photo in the menu).
The user can edit this information (besides the necessary login information).

#### Home screen
![image](https://user-images.githubusercontent.com/44946807/88774409-153a7280-d18c-11ea-8833-70a31e41f08e.png)

The home screen for each user will show driving lessons that he will have or lessons that have already been done.
You can scroll right or left to switch between dates.
Each date has a list of driving lessons on the same day, with student / teacher details, phone and time.
If the student / teacher does not have lessons, this screen will remain blank.
You can click on one of the lessons to see more details.

#### Teachers search
![image](https://user-images.githubusercontent.com/44946807/88775028-dbb63700-d18c-11ea-8b55-78805d8ecf28.png)

Only student-type users have access to this screen.
The screen includes all the teachers who have registered for the system.
Teachers can be filtered by 4 categories:
- `location`
- `vehicle type`
- `gear type`
- `price`

![image](https://user-images.githubusercontent.com/44946807/88775862-e9b88780-d18d-11ea-81d5-5315cbc74eae.png)  ![image](https://user-images.githubusercontent.com/44946807/88775516-77e03e00-d18d-11ea-8452-080203518556.png)  


After selecting a suitable teacher.
- The student can go in and **see the details** of the teacher.
- The student can **call** to the teacher.
- The student can send an **email** to the teacher.
- The student can submit a **login request** to schedule shared driving lessons in the future.

#### Requests
![image](https://user-images.githubusercontent.com/44946807/88776402-9266e700-d18e-11ea-9987-feae4cf51dbe.png)

Teacher-only users have access to this screen.
In this screen the teacher can see all the student requests he has received, **including their personal details**.
An application that has not yet been approved will have two buttons: `Accept` or `Reject`.

**A rejected request will disappear the next time we open the screen.**

#### Determining driving lessons
![image](https://user-images.githubusercontent.com/44946807/88776925-4c5e5300-d18f-11ea-97d2-8887b68bd215.png)

For student-only users, a floating action button will appear on the side of the screen.
- By clicking on the plus you can determine a future driving lesson provided that the student has a teacher.
- A student will not be able to schedule a lesson during hours when the teacher has a lesson with another student, he will have the option to schedule a lesson at another time.
- If the student does not have a teacher who has approved him, he will have the option to go to the teacher search screen.
- A student can choose what type of lesson he wants to determine:
`Regular lesson (one hour)`, 
`Double lesson (two hours)`

![image](https://user-images.githubusercontent.com/44946807/88778044-a875a700-d190-11ea-811a-33b26c438578.png)     ![image](https://user-images.githubusercontent.com/44946807/88778107-c3481b80-d190-11ea-969f-87c336396a1f.png) ![image](https://user-images.githubusercontent.com/44946807/88778263-f7bbd780-d190-11ea-8d1b-c079ff914742.png)   ![image](https://user-images.githubusercontent.com/44946807/88778320-0e622e80-d191-11ea-8e7b-729154ff6499.png)



