package com.example.jposgateway;

import com.example.jposgateway.utils.JsonToIsoConverter;
import com.example.jposgateway.utils.TransactionRequest;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.MUX;
import org.jpos.q2.Q2;
import org.jpos.q2.iso.QMUX;
import org.jpos.util.NameRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class JposGatewayApplication{
	@Autowired
	private JsonToIsoConverter jsonToIsoConverter;
	public static void main(String[] args) {
		SpringApplication.run(JposGatewayApplication.class, args);
	}

	@Bean
	public Q2 Q2(){
		Q2 q2 = new Q2();
		q2.start();
		return q2;
	}
	@GetMapping("/test")
	public String requesttest() throws ISOException, NameRegistrar.NotFoundException {
		ISOMsg connectMsg = new ISOMsg();
		connectMsg.setMTI("1800");
		connectMsg.set(11, "123456"); // Numéro de référence
		connectMsg.set(33, "123456"); // Numéro de terminal
		MUX mux = QMUX.getMUX("gateway-mux");
		//System.out.println(mux.isConnected());
		ISOMsg connectResp =  mux.request(connectMsg,30000);
		if(connectResp!=null){
			if(connectResp.getMTI().equals("1810") && connectResp.getString(39).equals("00")){
				System.out.println("Connection established successfully");
				// Créer un message de type 1200 pour effectuer une transaction
				ISOMsg transMsg = new ISOMsg();
				transMsg.setMTI("1200");
				transMsg.set(2, "1234567890123456"); // Numéro de carte
				transMsg.set(3, "000000"); // Code de transaction
				transMsg.set(4, "000000001000"); // Montant de la transaction
				transMsg.set(11, "123456"); // Numéro de référence
				transMsg.set(33, "123456"); // Numéro de terminal
				ISOMsg transResp = mux.request(transMsg,3000);
				if(transResp.getMTI().equals("1210") && transResp.getString(39).equals("00")){
					return "Transaction successfully completed";
				}
				else{
					return "transaction error : " + transResp.getString(39);
				}
			}
			else{
				return "Conection error : " + connectResp.toString()+" "+connectResp.getString(39);
			}
		}
		else{
			return "network problem !!";
		}
	}
	@PostMapping("/transaction")
	public String pay(@RequestBody TransactionRequest transactionRequest) throws ISOException {
		ISOMsg isoMsg = jsonToIsoConverter.convertJsonToIso(transactionRequest);
		if(isoMsg.getString(4)!= null){
			System.out.println("Received ISO8583 message: "+isoMsg);
			return "well done";
		}
		else
			return "Something wrong !!";
	}
}
