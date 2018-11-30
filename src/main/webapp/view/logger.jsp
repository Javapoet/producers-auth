<%!
    org.apache.logging.log4j.Logger logger;
    public void jspInit() {
        logger = org.apache.logging.log4j.LogManager.getLogger();
        logger.debug("jspInit()");
    }
%>
