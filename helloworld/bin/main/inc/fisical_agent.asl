
//Agent Smith

+count(6) <- .print("Comunicando com o sensor!").

+count(8) <- comunicacaoArduino.

{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
{ include("$moise/asl/org-obedient.asl") }