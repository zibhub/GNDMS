---
title: GNDMS Developers are using IntelliJ
root: ../../../..
layout: post
---
Since we spend a lot of time in the past month refactoring and rewriting
old GNDMS-Code to new Spring REST-based GNDMS, I want to use this post to
thank our sponsor: JetBrains.

Approx nine month ago we applied for an IntelliJ Open Source Project License for the GNDMS project and received free keys for the IntelliJ Ultimate Java IDE version 10. 

Using IntelliJ in this stage of our project turned out to be a major advantage. Since we had to refactor zillion lines of old code, the awesome inspection and refactoring features saved us from a lot of grieve and pain. My favorite inspections so far turned out to be 
   - _Illegal package dependency_, which lets you define scopes in which packages are or are not allowed to be used, and
   - _Unused declaration_, which is helpful to eliminate unused classes or to find missing calls to classes, which might have gotten under the wheels during rewriting and moving around classes.

The second advantage we gained from using IntelliJ is the incredible support for the Spring framework. Using Spring we heavily depend on Spring XML-based application contexts to wire together the application logic and provide well-known objects. Using IntelliJ it is very easy to keep track of the various places where classes are instantiated and where which object is injected. 

So we just want to say a big thank you JetBrains for this great IDE and of course the free license.

Your 
GNDMS-development team
