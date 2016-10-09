# Developer Guide 

* [Setting Up](#setting-up)
* [Design](#design)
* [Implementation](#implementation)
* [Testing](#testing)
* [Dev Ops](#dev-ops)
* [Appendix A: User Stories](#appendix-a--user-stories)
* [Appendix B: Use Cases](#appendix-b--use-cases)
* [Appendix C: Non Functional Requirements](#appendix-c--non-functional-requirements)
* [Appendix D: Glossary](#appendix-d--glossary)
* [Appendix E : Product Survey](#appendix-e-product-survey)

<br>
## Setting up

#### Prerequisites

1. **JDK `1.8.0_60`**  or later<br>

    > Having any Java 8 version is not enough. <br>
    This app will not work with earlier versions of Java 8.
    
2. **Eclipse** IDE
3. **e(fx)clipse** plugin for Eclipse (Do the steps 2 onwards given in
   [this page](http://www.eclipse.org/efxclipse/install.html#for-the-ambitious))
4. **Buildship Gradle Integration** plugin from the Eclipse Marketplace

<br>
#### Importing the project into Eclipse

0. Fork this repo, and clone the fork to your computer
1. Open Eclipse (Note: Ensure you have installed the **e(fx)clipse** and **buildship** plugins as given 
   in the prerequisites above)
2. Click `File` > `Import`
3. Click `Gradle` > `Gradle Project` > `Next` > `Next`
4. Click `Browse`, then locate the project's directory
5. Click `Finish`

  > * If you are asked whether to 'keep' or 'overwrite' config files, choose to 'keep'.
  > * Depending on your connection speed and server load, it can even take up to 30 minutes for the set up to finish
      (This is because Gradle downloads library files from servers during the project set up process)
  > * If Eclipse auto-changed any settings files during the import process, you can discard those changes.

<br>
#### Troubleshooting

**Problem: Eclipse reports compile errors after new commits are pulled from Git**
* Reason: Eclipse fails to recognize new files that appeared due to the Git pull. 
* Solution: Refresh the project in Eclipse:<br> 
  Right click on the project (in Eclipse package explorer), choose `Gradle` -> `Refresh Gradle Project`.
  
**Problem: Eclipse reports some required libraries missing**
* Reason: Required libraries may not have been downloaded during the project import. 
* Solution: [Run tests using Gradle](UsingGradle.md) once (to refresh the libraries).
 

<br> 
## Design

### Architecture

<img src="images/UML_Architecture.png" width="600"><br>
<br>
The **_Architecture Diagram_** given above explains the high-level design of the App.
Given below is a quick overview of each component.

`Main` has only one class called [`MainApp`](../src/main/java/seedu/address/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connect them up with each other.
* At shut down: Shuts down the components and invoke cleanup method where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.
Two of those classes play important roles at the architecture level.
* `EventsCentre` : This class (written using [Google's Event Bus library](https://github.com/google/guava/wiki/EventBusExplained))
  is used by components to communicate with other components using events (i.e. a form of _Event Driven_ design)
* `LogsCenter` : Used by many classes to write log messages to the App's log file.

The rest of the App consists four components.
* [**`UI`**](#ui-component) : The UI of the App.
* [**`Logic`**](#logic-component) : The command executor.
* [**`Model`**](#model-component) : Holds the data of the App in-memory.
* [**`Storage`**](#storage-component) : Reads data from, and writes data to, the hard disk.

Each of the four components
* Defines its _API_ in an `interface` with the same name as the Component.
* Exposes its functionality using a `{Component Name}Manager` class.

For example, the `Logic` component (see the class diagram given below) defines it's API in the `Logic.java`
interface and exposes its functionality using the `LogicManager.java` class.<br>
<br>
<img src="images/UML_Logic.png" width="800"><br>
<br>

The _Sequence Diagram_ below shows how the components interact for the scenario where the user issues the
command `delete 3`.

<img src="images\UML_Seq1.png" width="800">

>Note how the `Model` simply raises a `EmeraldoChangedEvent` when the Emeraldo data are changed,
 instead of asking the `Storage` to save the updates to the hard disk.

<br>
 
The diagram below shows how the `EventsCenter` reacts to that event, which eventually results in the updates
being saved to the hard disk and the status bar of the UI being updated to reflect the 'Last Updated' time. <br>
<br>
<img src="images\UML_Seq2.png" width="800">

> Note how the event is propagated through the `EventsCenter` to the `Storage` and `UI` without `Model` having
  to be coupled to either of them. This is an example of how this Event Driven approach helps us reduce direct 
  coupling between components.

The sections below give more details of each component.

<br>
### UI component

<img src="images/UML_UI.png" width="800"><br>

**API** : [`Ui.java`](../src/main/java/seedu/address/ui/Ui.java)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `TaskListPanel`,
`StatusBarFooter`, etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class
and they can be loaded using the `UiPartLoader`.

The `UI` component uses JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files
 that are in the `src/main/resources/view` folder.<br>
 For example, the layout of the [`MainWindow`](../src/main/java/seedu/address/ui/MainWindow.java) is specified in
 [`MainWindow.fxml`](../src/main/resources/view/MainWindow.fxml)

The `UI` component,
* Executes user commands using the `Logic` component.
* Binds itself to some data in the `Model` so that the UI can auto-update when data in the `Model` change.
* Responds to events raised from various parts of the App and updates the UI accordingly.

<br>
### Logic component

<img src="images/UML_Logic.png" width="800"><br>

**API** : [`Logic.java`](../src/main/java/seedu/address/logic/Logic.java)

1. `Logic` uses the `Parser` class to parse the user command.
2. This results in a `Command` object which is executed by the `LogicManager`.
3. The command execution can affect the `Model` (e.g. adding a task) and/or raise events.
4. The result of the command execution is encapsulated as a `CommandResult` object which is passed back to the `Ui`.

Given below is the Sequence Diagram for interactions within the `Logic` component for the `execute("delete 1")`
 API call.<br>
<br>
<img src="images/UML_Seq3.png" width="800"><br>
<br>

### Model component

<img src="images/UML_Model.png" width="800"><br>

**API** : [`Model.java`](../src/main/java/seedu/address/model/Model.java)

The `Model`,
* stores a `UserPref` object that represents the user's preferences.
* stores the Emeraldo data.
* exposes a `UnmodifiableObservableList<ReadOnlyPerson>` that can be 'observed' e.g. the UI can be bound to this list
  so that the UI automatically updates when the data in the list change.
* does not depend on any of the other three components.
<br>
<br>

### Storage component

<img src="images/UML_Storage.png" width="800"><br>

**API** : [`Storage.java`](../src/main/java/seedu/address/storage/Storage.java)

The `Storage` component,
* can save `UserPref` objects in json format and read it back.
* can save the Emeraldo data in xml format and read it back.
<br>

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.
<br>
<br>

## Implementation

### Logging

We are using `java.util.logging` package for logging. The `LogsCenter` class is used to manage the logging levels
and logging destinations.

* The logging level can be controlled using the `logLevel` setting in the configuration file
  (See [Configuration](#configuration))
* The `Logger` for a class can be obtained using `LogsCenter.getLogger(Class)` which will log messages according to
  the specified logging level
* Currently log messages are output through: `Console` and to a `.log` file.

**Logging Levels**

* `SEVERE` : Critical problem detected which may possibly cause the termination of the application
* `WARNING` : Can continue, but with caution
* `INFO` : Information showing the noteworthy actions by the App
* `FINE` : Details that is not usually noteworthy but may be useful in debugging
  e.g. print the actual list instead of just its size
<br>
  
### Configuration

Certain properties of the application can be controlled (e.g App name, logging level) through the configuration file 
(default: `config.json`):
<br>

## Testing

Tests can be found in the `./src/test/java` folder.

**In Eclipse**:
* To run all tests, right-click on the `src/test/java` folder and choose
  `Run as` > `JUnit Test`
* To run a subset of tests, you can right-click on a test package, test class, or a test and choose
  to run as a JUnit test.

**Using Gradle**:
* See [UsingGradle.md](UsingGradle.md) for how to run tests using Gradle.

We have two types of tests:

1. **GUI Tests** - These are _System Tests_ that test the entire App by simulating user actions on the GUI. 
   These are in the `guitests` package.
  
2. **Non-GUI Tests** - These are tests not involving the GUI. They include,
   1. _Unit tests_ targeting the lowest level methods/classes. <br>
      e.g. `seedu.address.commons.UrlUtilTest`
   2. _Integration tests_ that are checking the integration of multiple code units 
     (those code units are assumed to be working).<br>
      e.g. `seedu.address.storage.StorageManagerTest`
   3. Hybrids of unit and integration tests. These test are checking multiple code units as well as 
      how the are connected together.<br>
      e.g. `seedu.address.logic.LogicManagerTest`
  
**Headless GUI Testing** :
Thanks to the [TestFX](https://github.com/TestFX/TestFX) library we use,
 our GUI tests can be run in the _headless_ mode. 
 In the headless mode, GUI tests do not show up on the screen.
 That means the developer can do other things on the Computer while the tests are running.<br>
 See [UsingGradle.md](UsingGradle.md#running-tests) to learn how to run tests in headless mode.
 
#### Troubleshooting tests
 **Problem: Tests fail because NullPointException when AssertionError is expected**
 * Reason: Assertions are not enabled for JUnit tests. 
   This can happen if you are not using a recent Eclipse version (i.e. _Neon_ or later)
 * Solution: Enable assertions in JUnit tests as described 
   [here](http://stackoverflow.com/questions/2522897/eclipse-junit-ea-vm-option). <br>
   Delete run configurations created when you ran tests earlier.


<br>
## Appendix A : User Stories

#### High Priority `* * *`

As a ... | I want to ... | so that ...
---------|--------------|------------
new user | see usage instructions | I can refer to instructions when i use the Task Manager.<br>
new user | view more information about a particular command | I can learn how to use various commands.<br>
user | add a task by specifying a task description and the date and time | I can record tasks that need to be done by that date and time.<br>
user | add a scheduled event by specifying the event name and duration and location | I can record events that I need to attend.<br>
user | add a task by specifying a task description only | I can record tasks that need to be done ‘some day’.<br>
user | delete a task | I can remove entries that I no longer need.<br>
user | list all tasks | I can see my tasks in a glance.<br>
user | edit the task descriptions, date or time | I can update the details of my tasks.<br>

<br>
#### Medium Priority `* *`

As a ... | I want to ... | so that ...
---------|--------------|------------
user | undo my previous actions | I can revert any mistakes made.<br>
user | sort the tasks by the date and time | I can see my tasks according to their urgency.<br>
user | search for my tasks using some keywords | I can easily view tasks based on the specified keywords.<br>
user | mark tasks as completed or uncompleted | I can keep track of my tasks progress.<br>
user | specify a folder for data storage location | I can store the data file in a local folder controlled by a cloud syncing service.<br>

<br>
#### Low Priority `*`

As a ... | I want to ... | so that ...
---------|--------------|------------
user | add tags to my task | I can categorise my tasks.<br>
user | input my dates and days in any format that I want | | it is easier for my usage.<br>
user | implement recurring tasks | I don’t have to add the task every week.<br>
advanced user | use shorter versions of the commands | I can type the commands faster.<br>
busy user | know what I can do next | I can fill up my free time slots.<br>
busy user | reserve time slots for one or more tasks that may not be confirmed | I know which to prioritise


<br>
## Appendix B : Use Cases

(For all use cases below, the **System** is the Task Manager and the **Actor** is the user, unless specified otherwise)

####Use case: Add task

**MSS**

1. User requests to add task
2. TaskManager adds task and confirms that user has added the task 

**Extensions**

2a. The task has the exact same details as an existing task

> TaskManager shows error message: “This task has already been created.”
	
<br>
#### Use case: Edit task

**MSS**

1. User requests to list task
2. TaskManager shows a list of task
3. User requests to edit a specific field of the task
4. TaskManager edits the field

**Extensions**

2a. The list is empty

> Use case ends

3a. The given index is invalid

> 3a1. TaskManager shows an error message
  Use case resumes at step 2

3b. The given field tag is empty

> 3b1. TaskManager asks to add to the field instead
> 3b2. User confirms addition
> 3b3. TaskManager adds to the specified field

<br>
#### Use case: Delete task

**MSS**

1. User requests to list task
2. TaskManager shows a list of task
3. User requests to delete a specific task in the list
4. TaskManager display confirmation message
5. User enters ‘Yes’ to confirm delete
6. TaskManager deletes the task 
Use case ends.

**Extensions**

2a. The list is empty

> Use case ends

3a. The given index is invalid

> 3a1. TaskManager shows an error message
  Use case resumes at step 2

5a. The user input is invalid

> 5a1. TaskManager shows an error message
> 5a2. User enters input
Step 5a1 and 5a2 are repeated until the user enters a valid input

<br>
#### Use case: List by
	
**MSS**

1. User requests to list by a certain category
2. TaskManager shows a list of task of that category

**Extensions**

2a. Category is unavailable

> Use case ends
	
2b. Category is by date

> TaskManager shows list of tasks by date, using start time of EVENTS and end time of DEADLINES

2c. Category is by tag

> TaskManager shows list of tasks by the particular tag in order of index


<br>
## Appendix C : Non Functional Requirements

1. Should work on any [mainstream OS](#mainstream-os) as long as it has Java `1.8.0_60` or higher installed.
2. Should be able to hold up to 1000 tasks.
3. Should come with automated unit tests and open source code.
4. Should favor DOS style commands over Unix-style commands.

{More to be added}


<br>
## Appendix D : Glossary

##### Mainstream OS

> Windows, Linux, Unix, OS-X

<br>
## Appendix E : Product Survey

#### Wunderlist
 **Strengths**
  * Able to add items to any list and assign deadlines and reminders
  * Requires few taps/interaction to create a simple to-do item
  * Natural language feature lets user type simple due dates, such as “tomorrow”, and Wunderlist will interprets and assigns accordingly
  * Available on multiple platforms, access easily from laptop or phone
  * Simple interface
  * Able to share lists with others for collaboration
  * Able to sort list

#### Todoist
 **Strengths**
  * Able to identify dates in tasks statements e.g 18oct, mon. Useful for Jim’s command line habits
  * Has smartphone and desktop applications. Useful for Jim’s requirement for portability
  * Has undo option
  * Allows the use of tags to classify different tasks e.g personal, school work.
  * Allows users to set priority levels to tasks
  * Has smartphone notifications for tasks due today
  * Multiple display formats: today, next 7 days, by tags

**Weaknesses**
  * Synced to the cloud. Users may have problems when there is no internet connection

#### Centrallo
 **Strengths**
  * Supports email forwarding from your personal mail into Centrallo
  * Allows the creation of list, notes and checklist, where the notes and checklist can be filed under a particular list
  * Items can be tagged as priorities which is consolidated in a ‘priorities’ tab
  * Notes, lists and checklists can be given different colours (7 to choose from)
  * Supports attachment of files (up to 25mb) to the notes
  * Offers 3 methods to sort the notes i.e. alphabetically, by created date and by updated date

**Weaknesses**
  * Only can move 1 note/checklist at a time
  * Limitations of 100 notes in total for a free account
  * Requires internet connection to access (no offline version)
  * No calendar view for a quick overview of all notes by dates
  * No quick overview of all notes in general (GUI highly resembles that of an email inbox - the notes occupy a narrow column on the screen with the rest of the screen occupied by the content of the notes)

#### Trello
 **Strengths**
  * Able to create various lists, allows good organisation
  * Able to add friends into the lists which allows communication between colleagues
  * Different types of lists can be created such as checklists, description texts, deadlines, labels and attachments
  * Can move, copy, subscribe, archive
  * Can be used on both mobile and desktop
  * Can set lists to be visible or invisible to others
  * Can use powerups which has special add ons to the lists, thus Jim can customise it to his needs
  * Has reminders
  * Activity summary to allow Jim to know what he did earlier
  * Can share lists which Jim can tell his colleagues tasks he needs to do/the team needs to do

**Weaknesses**
  * Too many options, too troublesome for Jim’s need to use single line command
  * Difficult to look for things Jim needs
  * No calendar view
  * More useful for teams than individuals
