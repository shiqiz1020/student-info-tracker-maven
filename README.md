# Student Info Tracker Java Web App

## Tools and Technology used
* JDK - 1.8 (This is required for jpf-symbc)
* JSP - 2.3
* JSTL - 1.2.1
* [Apache Tomcat v9.0](https://tomcat.apache.org/download-90.cgi)
* [MySQL v8](https://dev.mysql.com/downloads/mysql/)
* [Eclipse](https://www.eclipse.org/downloads/) (Enterprise Edition)
* Apache Maven

## Configure Java Path Finder
### Download
Install Ant:
```
brew install ant
```

Download junit.jar and hamcrest.jar [here](https://github.com/junit-team/junit4/wiki/Download-and-Install) and place them in junit directory.

Create a JPF home directory:
```
mkdir JPF_HOME
cd JPF_HOME
```
Clone jpf-core from here: https://github.com/yannicnoller/jpf-core.

Clone jpf-symbc from here: https://github.com/SymbolicPathFinder/jpf-symbc

### Configure site.properties
Create a `.jpf` dir at ${user.home}:
```
cd ~
mkdir .jpf
```
Create a file named `site.properties` in ~/.jpf including the following content:
```
jpf.home = ${user.home}/path-to-JPF_HOME
junit.home = ${user.home}/path-to-junit
jpf-core = ${user.home}/path-to-JPF_HOME/jpf-core
jpf-symbc = ${user.home}/path-to-JPF_HOME/jpf-symbc

extensions=${jpf-core},${jpf-symbc}
```

### Configure build.xml
Build jpf-core:
```
cd ~/path-to-JPF_HOME/jpf-core
ant test
```
* If having compiler error: 
  ```
  [javac] jpf-core/src/main/gov/nasa/jpf/vm/MJIEnv.java:20: error: cannot find symbol
  [javac] import com.sun.org.apache.bcel.internal.generic.InstructionConstants;
  ```
  In JPF_HOME/jpf-core/src/main/gov/nasa/jpf/vm/MJIenv.java, comment out our delete the import 
  `import com.sun.org.apache.bcel.internal.generic.InstructionConstants;` 

* If build failed because ${env.JUNIT_HOME} does not exist, open `build.xml` and replace all ${env.JUNIT_HOME} with the path to junit.

* Note: I'm failing `gov.nasa.jpf.test.java.io.ObjectStreamTest`, still need to figure out why.

Build jpf-symbc:
```
cd ~/path-to-JPF_HOME/jpf-symbc
ant test
```
* If build failed because "The JUNIT_HOME environment variable must be set", replace all ${junit.home} with the path to junit in builx.xml.

## Configure Eclipse and Tomcat
Double check configurations are correct in Eclipse->Preferences:
* Java -> Compiler-> JDK Compliance = 1.8
* Java -> Installed JREs -> select Java 8

Connect Tomcat in Ecliplse:
* Click Project Explorer -> click Server (at the bottom)
* Apache -> Tomcat v9.0 Server -> Select Tomcat installation directory (where Tomcat is downloaded) -> Finish

## Running This Project
* Clone the repo
* Import into Eclipse as existing Maven project
  * File -> Import -> Existing Maven Projects -> Next -> select root directory -> Finish
  * Right-click the project and select Properties. Make sure 1) Java Build Path -> Libraries has Java 8 JRE library and Tomcat 9.0, 2) Java Compiler -> JDK compliance is at 1.8.
* Right-click project -> Run as -> Maven Build -> Goal = clean install
* Use the scripts in `sql-scripts` folder to create the `web-student-tracker` database
* Right-click student-info-tracker/WebContent/list-students.jsp -> Run As -> Run on server -> select Tomcat 9
* Now can view the page on localhost:8080
