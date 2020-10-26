// Agent speacker1 in project first_attempt_jacamo

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start <- 
	.send(speaker_2, tell, v(10)).

+!g(X)
	<- print("achive ",X ).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
