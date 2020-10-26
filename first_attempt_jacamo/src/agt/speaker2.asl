// Agent speacker2 in project first_attempt_jacamo

/* Initial beliefs and rules */

/* Plans */

+v(X) [source(Who)]
	<- println("new belief acquired ", v(X), " by ", Who)
	   .send(Who, achieve, g(9)).	

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
