// **********************************************************************
//
// Copyright (c) 2003-2013 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.5.1
//
// <auto-generated>
//
// Generated from file `benchmark.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package dfki.sb.zerociceproject.Main;

public interface BenchmarkPrx extends Ice.ObjectPrx
{
    public MarketData sendMarketData(MarketData marketDataArg);

    public MarketData sendMarketData(MarketData marketDataArg, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_sendMarketData(MarketData marketDataArg);

    public Ice.AsyncResult begin_sendMarketData(MarketData marketDataArg, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_sendMarketData(MarketData marketDataArg, Ice.Callback __cb);

    public Ice.AsyncResult begin_sendMarketData(MarketData marketDataArg, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_sendMarketData(MarketData marketDataArg, Callback_Benchmark_sendMarketData __cb);

    public Ice.AsyncResult begin_sendMarketData(MarketData marketDataArg, java.util.Map<String, String> __ctx, Callback_Benchmark_sendMarketData __cb);

    public MarketData end_sendMarketData(Ice.AsyncResult __result);

    public QuoteRequest sendQuoteRequest(QuoteRequest quoteRequestArg);

    public QuoteRequest sendQuoteRequest(QuoteRequest quoteRequestArg, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_sendQuoteRequest(QuoteRequest quoteRequestArg);

    public Ice.AsyncResult begin_sendQuoteRequest(QuoteRequest quoteRequestArg, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_sendQuoteRequest(QuoteRequest quoteRequestArg, Ice.Callback __cb);

    public Ice.AsyncResult begin_sendQuoteRequest(QuoteRequest quoteRequestArg, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_sendQuoteRequest(QuoteRequest quoteRequestArg, Callback_Benchmark_sendQuoteRequest __cb);

    public Ice.AsyncResult begin_sendQuoteRequest(QuoteRequest quoteRequestArg, java.util.Map<String, String> __ctx, Callback_Benchmark_sendQuoteRequest __cb);

    public QuoteRequest end_sendQuoteRequest(Ice.AsyncResult __result);
}
