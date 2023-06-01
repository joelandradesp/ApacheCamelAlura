package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class RotaPedidos {

	public static void main(String[] args) throws Exception {

		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				
				from("file:pedidos?delay=5s&noop=true").
				split().xpath("/pedido/itens/item").
				//log("${id}").
				//log("${body}").
				//filter().xpath("pedido/itens/item/formato[text()='EBOOK']").
				filter().xpath("/item/formato[text()='EBOOK']").
				log("${id} \n ${body}").
				marshal().xmljson().
				//log("${body}").
				// Sempre vai ser o mesmo nome e o último Xml lido transformado em Json.
				// setHeader("CamelFileName", constant("pedido.json")).
				setHeader("CamelFileName", simple("${file:name.noext}.json")).
				to("file:saida");
				
			}
			
		});
		
		context.start();
		Thread.sleep(20000);
		context.stop();

	}	
}
