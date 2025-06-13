# DSA-Visualised
DSA-Visualised is a web-based educational platform for visualising and interacting with fundamental Data Structures and Algorithms (DSA). Built with Jakarta EE, it runs on a GlassFish server and uses a Derby database to manage users and educational content.

Technologies Used:
- Java EE (Jakarta EE)
- JSF (JavaServer Faces)
- GlassFish Server (7.0.6)
- Apache Derby
- NetBeans IDE (Netbeans 20)
- HTML / CSS / JavaScript for UI and animations

# How to run:
Follow these steps to set up and run the project on your local machine using NetBeans, GlassFish, and Derby.

1 - Clone repository:
  - git clone https://github.com/JoseSilva1997/DSA-Visualised.git

2 - Open Project on Netbeans:
  - Open NetBeans IDE.
  - Go to File → Open Project.
  - Navigate to the cloned folder and open the project.

3 - Setup the database (Apache Derby):
  - Go to the database + data folder in the project root.
  - Use NetBeans’ Services > Databases tab, or any Derby tool, to:
      - Create a new Apache Derby (JavaDB) database.
      - Apply the schema and data from the "database + data " file in the folder.
  - Note the location and name of your database — you'll need this for the next step.

4 - Configure JDBC in GlassFish:
  You must replicate the exact JNDI name used in your persistence.xml file. Follow these steps:

  4.1 - Create a JDBC Connection Pool:
    - Start GlassFish and open the Admin Console: http://localhost:4848

  4.2 - Go to:
    - Resources → JDBC → JDBC Connection Pools → New

  4.3 - Fill out:
    - Name: DSA_ConnectionPool
    - Resource Type: javax.sql.DataSource
    - Database Vendor: Derby
  
  4.4 - Click Next, and under Additional Properties, set:
    - User: APP
    - Password: APP (or as defined)
    - DatabaseName: full path to your Derby DB (e.g., C:/Users/yourname/Derby/DSA_DB)
    - ServerName: localhost
    - PortNumber: 1527

Click Finish, then ping the pool to make sure it's working.

5 - Create a JDBC Resource
  5.1 - Go to:
    - Resources → JDBC → JDBC Resources → New
  
  5.2 - Fill out:
    - JNDI Name: jdbc/DSA_DB
    - Pool Name: DSA_ConnectionPool

  - This name must match the value in your persistence.xml: <jta-data-source>jdbc/DSA_DB</jta-data-source>

6 - Deploy the Project
  - In NetBeans, right-click the project → Run or Deploy.
  - Make sure GlassFish is the selected server.
  - Wait for a successful deployment message.

7 - Access the Web App
  Open your browser and visit:
    - http://localhost:8080/DSA-Visualised/
