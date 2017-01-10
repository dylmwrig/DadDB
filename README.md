# DadDB
SQLite management software for my dad.

##Unfortunately, because I did not commit my original completed work to github, there is currently a bug in the program and it does not work. This arose when I was trying to add some features to the original completed program. When I have time I'll iron it out and complete the additional features. There were a few other things I want to clean up as well: originally I performed some SQLite database actions in my GUI class, which I later found out was bad practice. I'm still in the middle of transferring everything of that sort over to a "DatabaseAccess" class.

Originally I wanted to make this an android application but Christmas was coming too fast

##Intended use
I noticed my dad, before leaving for comic conventions or comic shops, having to physically write down every issue he needed for his collection on a piece of paper. This was very time consuming and not very thorough, he would oftentimes end up accidentally buying a second copy of the same issue. So I designed this to allow him to easily add everything in his collection then be able to sort through it based on different parameters which included the following:

1. Main Series title
2. Sub Series title
3. Issue number
4. Author name
5. Main artist name
6. Publisher name

So using the above formatting, an example insertion (taken from my own collection) would be the following:

1. Punisher
2. Punisher: War Journal
3. 11
4. Carl Potts
5. Jim Lee
6. Marvel

At the moment, there are three ways of using this software while on the go. You can use the built in print function to print out every single entry you need, you can use the program on a laptop, or (what will most likely be used the most) you can email the .db file to yourself and use a third party app to view it. I realize these solutions are all less than ideal, and I plan to properly port this to android in the future.

##ID Number
Originally I tried using an autoincremented value as the primary key for the ID number of each comic, but found it difficult to use and removed too much control. Instead I am just using an ID integer variable and incrementing it when comics are added and decrementing it when they are deleted. This makes it so that I can delete and add many comics at a time without worrying about how autoincrement deals with it while also being able to remove entries without creating gaps in the numbers used.

##User Interface
This was done in Java, so I was able to handle the GUI using swing and WindowBuilder. I used GridBagLayout for my layout manager, JTable to display my table, and simple JTextFields/JButtons/JLabels for the rest.

####Buttons
Most of the buttons call a method within DatabaseAccess, each of which return a ResultSet which is used to display information to the user.

#####Add Comic
Calls the addComic method in DatabaseAccess. This calls an addData method in the Database class, where the information from each textbox is fed into an insert SQL statement.

#####Sort Comics
Modifies a sort query based on different parameters. If the user has something entered into a textbox, it will only return entries which match that entry. By default it will sort the entries by ID number in ascending order, but you can specify which parameter to order by and how to order it (ascending or descending).

#####Edit Comics
Edits the comic at the ID number given in the appropriate text box, changing that comic by whatever parameters were entered in the other entry boxes.

#####Clear
Clears the entry boxes.

######Load Comics
Loads in everything in order of ascending ID number. Mainly used for showing the table upon opening the application.

######Delete Comics
Deletes a comic at the given ID number or the given ID numbers (multiple number selection explained in the usability section).

####Exception Handling/Keeping Things Safe
My dad is admittedly not the most tech savvy person in the world so I had to be thorough with making everything respond clearly and remove any remote chance of bugs in every circumstance. Originally I had tried using JFormattedTextField to validate input, but making my own method for input validation ended up being simpler and gave me more control. Error messages pop up if the user tries to input any non-integer into an integer text box, either ID or issue. It will also give an error on inputting numbers into ID that are greater than the total ID number or less than 1.

When a user doesn't input anything in any of the text boxes when adding a comic, a default value will be inserted ("NONE" for text based entries, and "-1" for issue number as there shouldn't ever be a negative issue number)

####Usability
Since the original version was completed I've been trying to improve the ease of use of this program. I'm making it so that if you type input of the format number/dash/number into issue number when you are adding comics, it will add issues with the same parameters from the low number to the high. So for the above example, if you entered 7-11 for issue number when adding the comic, it would add issues 7 through 11 with each other parameter being identical (Main = punisher, Publisher = Marvel, etc.). Because many runs of comics stick with the same artist and author, I thought this would make the software faster to use.

I've also added a couple of common sense featuers that I'm not sure why I didn't think of before, like a button that clears every text box, in addition to displaying the updated database whenever a change is made (originally you had to press Load Comic every single time).
