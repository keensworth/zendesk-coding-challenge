# Zendesk Coding Challenge

This is a acommand line interface application to access tickets on a Zendesk user's account. It is capable of connecting to the Zendesk API and
submitting HTTP requests to retrieve single tickets or a page of tickets (using pagination). 
Details of the response are given to the user - more detail for single-ticket view, and brief details for page view. This program was written in Java.

## Prerequisites

- [Maven](https://maven.apache.org/download.cgi "Maven Download") 3.x (or greater) installed
- [JDK](https://www.oracle.com/java/technologies/javase-jdk16-downloads.html "JDK Download") 14 (or greater) installed

## How to run

1. Clone the project into desired directory

```
> git clone https://github.com/keensworth/zendesk-coding-challenge.git zcc
```

2. [IMPORTANT] - Navigate to the following directry and edit the config.properties file to include your Zendesk credentials. This can be done in your file explorer and favored
text editor.
```
...\zcc\src\main\resources\config.properties
```

It will look like

```
email=
token=
subdomain=
```

3. cd into the cloned directory.

```
> cd zcc
```

4. Build the project with Maven.

```
> mvn package
```

5. cd into the `target` directory.

```
> cd target
```

6. Run the following jar file.

```
> java -jar zendesk_coding_challenge-1.0-jar-with-dependencies.jar
```

7. Use the program, following the prompts.

## Usage

At any point during use, the user will have access to a list of commands they can execute, such as

```
(t) Fetch ticket by ID
(a) View all available tickets
(q) Quit
```

The user must simply enter the text within the parentheses for the choice they would like to select, and press enter/return. The only unique case is when you
choose to view a ticket. In that case, you will be prompted with

```
Ticket ID:
```

and you must enter the ID of the desired ticket.

Images of use are available at the bottom of this README.

## Design Choices

### Overview

This project was created under a Model View Controller design pattern. This is because the project was relatively interactive, and it helped separate user interaction
and console rendering from the API logic.

#### Model
- `ConsoleManager`: Manages overall program state, communicates to the Console and to the API
- `TicketManager`: Manages high level calls to the API for ticket retrieval

#### View
- `Console`: Handles all console printing, including prompts, queires, warnings, errors, and ticket/page display

#### Controller
- `ConsoleController`: Handles all user input and manipulates the state of ConsoleManager

#### API
- `ZendeskAPI`: Stores encrypted auth codes and host URL, accepts raw requests to the Zendesk API
- `TicketFetcher`: Low-level utility for making ticket-specific calls to the Zendesk API
- `UserFetcher`: Low-level utility for making user-specific calls to the Zendesk API (to retrieve names from IDs)

#### Other Utility
- `JSONParser`: Responsible for translating all JSON HTTP responses into Java objects and retrieving specific values

## Gallery

### Main screen

![Main Menu](https://imgur.com/NKbNUPl.png)

### Single-ticket view

![Single Ticket](https://imgur.com/msjqHkh.png)

### Ticket-page view

![Ticket Page](https://imgur.com/7Eg6tD6.png)

### Example warning message

![Warn](https://imgur.com/InqAn5X.png)

### Example error message

![Error](https://imgur.com/s2qh7oQ.png)



