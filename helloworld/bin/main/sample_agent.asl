
// Agent bob 

// CRENÇA
count_ideal(60) .

// CRENÇA + DESEJO
+count(N) : count_ideal(I) & math.abs(I-N) > 0
<- !count(I) .

+count(N) : count_ideal(I) & math.abs(I-N) = 0
<- off_count .
    
+count(60) <- wait(20000).

// INTENÇÃO

+!count(I) : count(N) & N<I
<- inc .

{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
// uncomment the include below to have an agent compliant with its organisation
{ include("$moise/asl/org-obedient.asl") }
