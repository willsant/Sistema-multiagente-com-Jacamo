// CArtAgO artifact code for project helloworld

package example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;


import cartago.*;
import group.chon.javino.Javino;


public class Counter extends Artifact {
	private int count;
	private int sensor = 0;
	private int pos;
	
	void init(int initialValue) {
		this.count = initialValue;
		//defineObsProperty("count", initialValue);
		defineObsProperty("sensor", this.sensor);
		defineObsProperty("state", "off");
		defineObsProperty("count", this.count);
		defineObsProperty("posicao", this.pos);
		//log("Count: " + getObsProperty("count").intValue());
		//log("Sensor: " + getObsProperty("sensor").intValue());
	}

	@OPERATION
	void inc() {
		ObsProperty prop = getObsProperty("count");
		//ObsProperty state = getObsProperty("state");
		//prop.updateValue(prop.intValue()+1);
		signal("tick");
		getObsProperty("state").updateValue("on");
		//this.execInternalOp("inc_get",1);
		//log("Counter on! "+ prop);
		//log(getObsProperty("state").stringValue());
		inc_get(1, null);
	}

	@OPERATION
	void off_count(){
		getObsProperty("state").updateValue("off");
		//getObsProperty("count").updateValue(3);
		//log("Counter off");
		try {
			java.util.concurrent.TimeUnit.SECONDS.sleep(10);
			//log("Sleep");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log("Not Sleep");
		}
	}

	@OPERATION
	void inc_get(int inc, OpFeedbackParam<Integer> newValueArg) {
		ObsProperty prop = getObsProperty("count");
		ObsProperty state = getObsProperty("state");
		if (state.stringValue().equals("on")) {
			int newValue = prop.intValue();
			prop.updateValue(this.count + inc);
			this.count = prop.intValue();
			//newValueArg.set(newValue);
			//log("Count: " + newValue);
			this.await_time(2000);
		}
		//log("Counter off!");
		if (this.count == 60){
			this.count = 3;
			getObsProperty("state").updateValue("on");
			getObsProperty("count").updateValue(3);
			//log("Count reiniciado! ");
		}
	}

	@OPERATION
    public void comunicando(){
		ObsProperty prop = getObsProperty("sensor");
		ObsProperty propPos = getObsProperty("posicao");
		double medicoes[] = { 0.0, 0.2, 3.8, 0.2, 1.2, 3.4, 0.2, 0.0};
		double soma = 0.0;
		this.pos = propPos.intValue();
		if (this.pos < 3) {
			if(this.pos == 2){
				soma = medicoes[this.pos] + medicoes[this.pos-1] + medicoes[this.pos-2];
				log("mm/h = "+ soma + ", nao precisa comunicar!");
				propPos.updateValue(this.pos + 1);
				this.pos = propPos.intValue();
			} else if(this.pos == 1){
				soma = medicoes[this.pos] + medicoes[this.pos-1];
				log("mm/h = "+ soma + ", nao precisa comunicar!");
				propPos.updateValue(this.pos + 1);
				this.pos = propPos.intValue();
			} else{
				log("mm/h = "+ soma + ", nao precisa comunicar!");
				propPos.updateValue(this.pos + 1);
				this.pos = propPos.intValue();
			}
		} else{
		//ByteBuffer buffer = StandardCharsets.UTF_8.encode(texto);
			soma = medicoes[this.pos] + medicoes[this.pos-1] + medicoes[this.pos-2] + medicoes[this.pos-3];
			if(soma >= 5.0){
				int resposta = (int)soma;
				String texto = "l|"+ resposta;
				try {
					HttpClient client = HttpClient.newHttpClient();
					HttpRequest request2 = HttpRequest.newBuilder()
						.POST(BodyPublishers.ofString(texto))
						.uri(URI.create("http://192.168.2.7:7896/iot/d?k=4jggokgpepnvsb2uv4s40d59ov&i=sensor040"))
						//.timeout(Duration.ofMinutes(1))
						.header("Content-Type", "text/plain")
						//.header("fiware-service", "openiot")
						//.header("fiware-servicepath", "/")
						//.header("Accept", "application/xml")
						.build();
					client.sendAsync(request2, BodyHandlers.ofString());
					log("mm/h = "+ resposta + ", comunicando com o IoT Agent!");
					propPos.updateValue(this.pos + 1);
					this.pos = propPos.intValue();
				} catch (Exception e) {
					throw new RuntimeException(e);
					//log(e.getMessage().toString());
				}
			} else{
				log("mm/h = "+ soma + ", nao precisa comunicar!");
				propPos.updateValue(this.pos + 1);
				this.pos = propPos.intValue();
			}	
		}
		if(this.pos > 10){
			this.pos = 0;
		} 
    }

	@OPERATION
	public void comunicacaoArduino(){
		ObsProperty sen = getObsProperty("sensor");
		Javino javino = new Javino();
		String port = "COM6";
		Boolean valor = javino.requestData(port, "getPercepts");
		String resposta = javino.getData();
		//this.sensor = Integer.parseInt();
		//sen.updateValue(this.sensor);
		//System.out.println("" + resposta);
		///*if (javino.listenArduino(port)){
		//	this.sensor = Integer.parseInt(javino.);
		//	sen.updateValue(this.sensor);
		//	System.out.println("" + sen.intValue());
        //log("Escutado!");
		javino.closePort();
		//} else {
		//	log("Não escutado");
		//}
	}
}


