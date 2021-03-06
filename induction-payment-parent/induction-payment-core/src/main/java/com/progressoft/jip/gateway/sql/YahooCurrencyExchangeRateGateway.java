package com.progressoft.jip.gateway.sql;

import java.util.Objects;

import com.progressoft.jip.datastructures.CurrencyExchangeRate;
import com.progressoft.jip.gateway.AbstractRestfullGateway;
import com.progressoft.jip.gateways.exceptions.CurrencyCodeNotFoundException;
import com.progressoft.jip.gateways.exceptions.InvalidRestfulResponseFormatException;
import com.progressoft.jip.gateways.exceptions.NullRestfulParserException;
import com.progressoft.jip.utilities.Utilities;
import com.progressoft.jip.utilities.restful.RestfulDataParser;
import com.progressoft.jip.utilities.restful.RestfulResponseFormat;
import com.progressoft.jip.utilities.restful.impl.YahooXmlResponse;



public class YahooCurrencyExchangeRateGateway extends AbstractRestfullGateway<YahooXmlResponse>
		 {

	private static final String YAHOO_SERVER = "http://query.yahooapis.com/v1/public/yql?q=";
	private static final String YAHOO_QUERY = "select * from yahoo.finance.xchange where pair in";
	private RestfulResponseFormat responseFormat;

	public YahooCurrencyExchangeRateGateway(RestfulResponseFormat format, RestfulDataParser<YahooXmlResponse> parser) {
		super(parser);
		if (Objects.isNull(format))
			throw new InvalidRestfulResponseFormatException();
		if (Objects.isNull(parser))
			throw new NullRestfulParserException();
		this.responseFormat = format;
	}

	@Override
	public CurrencyExchangeRate loadCurrencyExchangeRate(String codeFrom, String codeTo) {
		if (!isValidCode(codeFrom) || !isValidCode(codeTo)){
			System.out.println(codeFrom +" " + codeTo);
			throw new CurrencyCodeNotFoundException();}
		YahooXmlResponse response = super.response(url(codeFrom, codeTo));
		if (!isValidResponse(response))
			throw new CurrencyCodeNotFoundException();
		return new CurrencyExchangeRate(response.fromCode(), response.toCode(),
				Double.parseDouble(response.rate()));
	}

	private boolean isValidCode(String codeFrom) {
		return !Objects.isNull(codeFrom) && !codeFrom.isEmpty();
	}

	private boolean isValidResponse(YahooXmlResponse response) {
		return !"N".equals(response.toCode()) && !"A".equals(response.fromCode());
	}

	private String url(String codeFrom, String codeTo) {
		return YAHOO_SERVER + Utilities.utf8Encoded(YAHOO_QUERY + " (\"" + codeTo + codeFrom + "\")")
				+ "&env=store://datatables.org/alltableswithkeys&format=" + responseFormat;
	}

}