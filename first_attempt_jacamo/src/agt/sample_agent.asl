// Agent sample_agent in project first_project

/*
 * Source of our belivef now is US
 */
language("ita").
//language("jp").

/* Initial goals inject from myself */

//!hello_world("cesena").

/* Plans */
/* define plan for specific goal */
/* +!hello_world(X) <- .println("Hello world from ", X). */
/* https://liveunibo-my.sharepoint.com/personal/matteo_scucchia_studio_unibo_it/_layouts/15/onedrive.aspx?id=%2Fpersonal%2Fmatteo%5Fscucchia%5Fstudio%5Funibo%5Fit%2FDocuments%2FUniversit%C3%A0%2F5%20ANNO%2FPERVASIVE%20COMPUTING%2FPERVASIVE%5F2020%5F10%5F22%2Emkv&parent=%2Fpersonal%2Fmatteo%5Fscucchia%5Fstudio%5Funibo%5Fit%2FDocuments%2FUniversit%C3%A0%2F5%20ANNO%2FPERVASIVE%20COMPUTING&originalPath=aHR0cHM6Ly9saXZldW5pYm8tbXkuc2hhcmVwb2ludC5jb20vOnY6L2cvcGVyc29uYWwvbWF0dGVvX3NjdWNjaGlhX3N0dWRpb191bmlib19pdC9FWmxXcFFuSVdJQkVsZVNGS3ZxTlIzNEJWcGUwejEzOGR5YU15aHhJV3UzcWRBP3J0aW1lPW1BM3dTaEI0MkVn */
/* define a specific plan that 
 * consider the belief, as sequence of action
 */

+!hello_world(X) : language("eng")
	<- .println("Hello");
	   .println("world");
       .println("from ", X).
       
+!hello_world(X) : language("ita")
	<- .println("Ciao a tutti da ", X).

/* First attempt to execute a goal but in disadvantage environment */
+!hello_world(X) : language(X)
	<- .println("Sorry i don \\'t know ", X, "!!!!").	



/*Define plan triggering some subgoal */

+!printCurrentAndNext(X)
	<- .println("act1");
	!subgoalPrint(X,OUTPUT);    //sequentially not parallel stop the current intention
	!subgoalPrint(OUTPUT,OUTPUT2);
	.println("Done all subgoal").

+!subgoalPrint(X,OUTPUT)
	<- .println("Print ",X);
		OUTPUT=X+1.
	
+!print1and2(_)
	<- .println("Sorry but the print1and2 fail !").

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
