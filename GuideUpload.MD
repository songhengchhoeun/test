# How to publish and distribute your Android library
First of all, let’s assume that you have already developed your Android library, so you have a library module in Android Studio.
![enter image description here](https://miro.medium.com/max/1392/1*FoYazVYDj5FlCpQuX88v9w.png)
An example of a library module

The library will be published to _jCenter,_ a Maven repository hosted by [bintray.com](http://www.bintray.com/). The process is simple: the library is built locally in Android Studio, the artifacts will be upload to _bintray_ and then linked to _jCenter._

![enter image description here](https://miro.medium.com/max/1328/1*iExRbQhqoflgn3mJjuwY7w.png)

In this way, the developers that want to use your library has only to declare it in the _build.gradle_ file of their projects. For example:

    implementation 'kh.com.mysabay:sabaysdk:1.0.6-b10'

The first thing to do is to create a [Bintray](https://bintray.com/) account and for open source projects is FREE.

![enter image description here](https://miro.medium.com/max/1088/1*_tBskl_JTR6UbEYZD0sSpg.png)
Create a Bintray account

After the creation of the account, you have to choose a repository. Let’s go with Maven (I know, you are thinking “Why? I want to publish it to jCenter”. Well, as said above jCenter is a Maven repository so we have to choose Maven).

![enter image description here](https://miro.medium.com/max/544/1*iGCmnnoRg0WIAE8Qywaodg.png)
Choose Maven as repository

Now you can create a new package inside your Maven repository.

![enter image description here](https://miro.medium.com/max/1392/1*PP8lVgEaoTzjaOqmrC2Isw.png)
Add a new package to the Maven repository

![enter image description here](https://miro.medium.com/max/366/1*dZhP4R7gzMTVzdkriYFzHw.png)
Add basic information of the library

After adding all the basic information of your library, like name, description, license, tags, etc., the repository of the library is ready.
![enter image description here](https://miro.medium.com/max/1100/1*CYoMvJQtZu3i3PSgiB74Bw.png)

**Now, back to the code. I’ll take as reference, a library that I’ve developed, so you can dive into the real code if you want.**

First of all, you have to add some dependencies into the _build.gradle_ file of your project.

    classpath 'com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4'
    classpath 'com.github.dcendents:android-maven-gradle-plugin:2.1'

`...`

Add this point you need to create `publish.gradle` file and add the script 

> all script please look in publish.gradle file in sdk project

Next on _build.gradle_ file of your sdk module need add some script

    apply from: 'publish.gradle'

Remember to keep private the username and the apikey. There are many ways, for example, I saved them in the _local.properties:_

    user=<your-username> 
    apiKey=<your-api-key>

**Noted**
user and apiKey is getting from bintray user account.

Finally, it’s time to open the Terminal and launch the build and upload task (I recommend to use the _— info_ flag to have a better understanding of what it is going on).

    ./gradlew clean publish bintrayUpload --info

Or you can click right side of android studio

![enter image description here](https://i.ibb.co/6DmByhJ/gradle-upload.jpg)

If everything went well, the artifacts have been uploaded to bintray but not yet published.

![enter image description here](https://miro.medium.com/max/760/1*mQwixMjhUz7JUiyyDnHPyw.png)
New version uploaded but not yet published

To publish the library, you have to click the _Publish_ button on a banner that appears in the repository page of the library.
![enter image description here](https://miro.medium.com/max/1122/1*-B3JOLzBAO6-sQu7A3nHKg.png)

And now the last step. You have to link the library to jCenter. This process must be done only the first time. At the right bottom of the library repository page, there is a “_Add to jCenter_”  button.
![enter image description here](https://miro.medium.com/max/1102/1*D9LScYGoESnPK3VJ-TRRjA.png)
Link the library to jCenter

Send the request and wait for the approval.
![enter image description here](https://miro.medium.com/max/1340/1*pgF7SwSRMTh_VfitSncemQ.png)

Within max two hours, your library is published and linked to jCenter, as you can see in the repository page.

![enter image description here](https://miro.medium.com/max/912/1*Tmp88sDZCNhYphsK_fA29g.png)

**Thank you**
