package mumbler.truffle.parser;

import mumbler.truffle.MumblerException;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

public abstract class MumblerReadException extends MumblerException {
    private static final long serialVersionUID = 1L;

    public MumblerReadException(String message) {
        super(message);
    }

    @Override
    public Throwable fillInStackTrace() {
        SourceSection sourceSection = this.getSourceSection();
        Source source = sourceSection != null ? sourceSection.getSource() : null;
        String sourceName = source != null ? source.getName() : null;
        int lineNumber;
        try {
            lineNumber = sourceSection != null ? sourceSection.getLineLocation().getLineNumber() : -1;
        } catch (UnsupportedOperationException e) {
            /*
             * SourceSection#getLineLocation() may throw an UnsupportedOperationException.
             */
            lineNumber = -1;
        }
        StackTraceElement[] traces = new StackTraceElement[] {
                new StackTraceElement("mumbler", this.getMethodName(), sourceName,
                        lineNumber)
        };
        this.setStackTrace(traces);
        return this;
    }

    public abstract SourceSection getSourceSection();

    public abstract String getMethodName();
}
