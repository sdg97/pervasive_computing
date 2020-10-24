// Agent counter_user in project first_project
/*
 * Loop model json book page 30
 * Intention interleaved 
 * Round robin algoorithm
 * so plans are not atomic
 * but you can set 
 * No low level race condition
 */
/* Initial beliefs and rules */

/* Initial goals */
!test.

/* Plans */
//counter not unique identifier (same name but different workspace
+!test 
	<- makeArtifact("counter", "acme.Counter", [13], Id);
	   .println("counter id ", Id);
	   //subscribed and percive object its state is mapper in 
	   //the current agent belief
	   focus(Id);
	   inc [artifact_id(Id)];
	   .println("first inc done");
	   .wait(1500);
	   //dec is not an operation test fail!
	   inc [artifact_id(Id)];
	   .println("second inc done").
	     
-!test
	<- .println("Test fails").	

//plan for react to counter ObsProp change
@react_to_change [atomic]
+value(X) 
	<- .println("counter value change", X);
	   .wait(1000)
	   .println("done reaction").
	
+update(X) 
	<- println(X).   

//+!achive :  <- .print("hello world.").

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
