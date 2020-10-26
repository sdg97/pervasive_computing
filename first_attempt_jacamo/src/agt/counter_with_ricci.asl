// Agent counter_with_ricci in project first_attempt_jacamo

/* Initial beliefs and rules */

/* Initial goals */

//declarative way
!count(10).

//procedural way 
//bring_count_to(100)

/* Plans */

+!count(X): not count(_) // I don't know nothing
	<- println("jkeadhjkasdh")
	   println("Counter with ricci start")
	   !prepare.
	
+!count(X) : count(X)
	<- println("achived").
	
+!count(X) : count(Y) & X < Y
	<-	println("too small") 
		inc
	   	!count(X).
	   	
+!count(X) : count(Y) & X > Y
	<-	println("too big") 
		dec
	   	!count(X).

+!prepare
	<- lookupArtifact("myEnvArtifact", Id); //provare a fare una wait potrebbe infatti non essere pronta
	focus(Id);
	println("counting")
	!count(X). 
	
-!prepare
	<-
		println("prepare fail") 
		.wait(100)
		!prepare.
		 
{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
