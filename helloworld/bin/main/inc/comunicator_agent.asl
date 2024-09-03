
//Agent Alice

+count(14) <- .print("Verificando se precisa comunicar a medicao ao IoT Agent!").

+count(16) <- comunicando.

{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
{ include("$moise/asl/org-obedient.asl") }

