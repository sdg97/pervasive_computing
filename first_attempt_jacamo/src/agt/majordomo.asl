// Agent majordomo in project first_attempt_jacamo

/* Initial beliefs and rules */

/* Initial goals */

!setup_env.

/* Plans */

+!setup_env 
	<- 
		.wait(3000);
	   makeArtifact("myEnvArtifact", "acme.MyEnv", [5]);
	   println("ready.").

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
