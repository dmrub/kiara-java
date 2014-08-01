module Main {

struct MarketDataEntry {
    int mdUpdateAction;
    int mdPriceLevel;
    double        mdEntryType;
    int openCloseSettleFlag;
    int securityIDSource;
    int securityID;
    int rptSeq;
    double        mdEntryPx;
    int mdEntryTime;
    int          mdEntrySize;  // in original i32
    int numberOfOrders;
    double        tradingSessionID;
    double        netChgPrevDay;
    int tradeVolume;
    double        tradeCondition;
    double        tickDirection;
    double        quoteCondition;
    int aggressorSide;
    double        matchEventIndicator;

    double dummy1;
    int dummy2;
};

sequence<MarketDataEntry> MarketDataEntrySeq;

struct MarketData {
    bool    isEcho;
    int        counter;
    int        securityID;
    double    applVersionID;
    double    messageType;
    double    senderCompID;
    int       msgSeqNum;
    int       sendingTime;
    int       tradeDate;
    MarketDataEntrySeq  mdEntries;
};

struct RelatedSym {
    double    symbol;
    long       orderQuantity;
    int    side;
    long    transactTime;
    int    quoteType;
    int      securityID;
    int      securityIDSource;
    double dummy1;
    int dummy2;
};

sequence<RelatedSym>        RelatedSymSeq;

struct QuoteRequest {
    bool            isEcho;
    int      counter;
    int      securityID;
    double             applVersionID;
    double             messageType;
    double             senderCompID;
    int      msgSeqNum;
    int      sendingTime;
    double             quoteReqID;
    RelatedSymSeq        related;
};

interface Benchmark {
    MarketData sendMarketData(MarketData marketDataArg);
    QuoteRequest sendQuoteRequest(QuoteRequest quoteRequestArg);
};

};
