package samples.mediators;

import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public class DiscountQuoteMediator extends AbstractMediator {

	private static final Log log = LogFactory.getLog(DiscountQuoteMediator.class);

	private String discountFactor = "10";

	private String bonusFor = "10";

	private int bonusCount = 0;

	public DiscountQuoteMediator() {}

	public boolean mediate(MessageContext mc) {

		String price = mc.getEnvelope().getBody().getFirstElement().getFirstElement().
				getFirstChildWithName(new QName("http://services.samples/xsd", "last")).getText();

		//converting String properties into integers
		int discount = Integer.parseInt(discountFactor);
		int bonusNo = Integer.parseInt(bonusFor);
		double currentPrice = Double.parseDouble(price);

		//discounting factor is deducted from current price form every response
		Double lastPrice = new Double(currentPrice - currentPrice * discount / 100);

		//Special discount of 5% offers for the first responses as set in the bonusFor property
		if (bonusCount <= bonusNo) {
			lastPrice = new Double(lastPrice.doubleValue() - lastPrice.doubleValue() * 0.05);
			bonusCount++;
		}

		String discountedPrice = lastPrice.toString();

		mc.getEnvelope().getBody().getFirstElement().getFirstElement().getFirstChildWithName
				(new QName("http://services.samples/xsd", "last")).setText(discountedPrice);

		System.out.println("Quote value discounted.");
		System.out.println("Original price: " + price);
		System.out.println("Discounted price: " + discountedPrice);

		return true;
	}

	public String getType() {
		return null;
	}

	public void setTraceState(int traceState) {
		traceState = 0;
	}

	public int getTraceState() {
		return 0;
	}

	public void setDiscountFactor(String discount) {
		discountFactor = discount;
	}

	public String getDiscountFactor() {
		return discountFactor;
	}

	public void setBonusFor(String bonus) {
		bonusFor = bonus;
	}

	public String getBonusFor() {
		return bonusFor;
	}

}