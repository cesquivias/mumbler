package mumbler.truffle.parser;

import com.oracle.truffle.api.source.SourceSection;

/**
 * All datatypes that can be written literally (e.g., numbers, lists, symbols)
 * should implement this interface so the source location can be retrieved and
 * fed to Truffle.
 */
public abstract class Syntax<T> {
    private final T value;
    private final SourceSection sourceSection;
    /**
     * This syntax object was used in a define call with the give name.
     */
    private String name;

    public Syntax(T value, SourceSection sourceSection) {
        this.value = value;
        this.sourceSection = sourceSection;
    }

    /**
     * Removes this object's outer-most syntax information. To recursively
     * remove all syntax information call {@link #strip()}.
     *
     * @return The value with no syntax information.
     */
    public T getValue() {
        return this.value;
    }

    /**
     * @return The location in the source code where this syntax object is
     *         located.
     */
    public SourceSection getSourceSection() {
        return this.sourceSection;
    }

    /**
     * @return The name if this syntax object was in a define call. null otherwise.
     */
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Recursively removes the syntax information and returns the bare value. To
     * just strip of the outer-most syntax information, call {@link #getValue()}
     * .
     *
     * @return The literal value with no syntax information
     */
    public Object strip() {
        // Override for compound datatypes.
        return this.value;
    }

    @Override
    public String toString() {
        return "#'" + this.value;
    }
}
