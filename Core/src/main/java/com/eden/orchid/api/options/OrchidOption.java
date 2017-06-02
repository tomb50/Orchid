package com.eden.orchid.api.options;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.registration.Prioritized;

/**
 * Denotes a Javadoc-style command-line argument. It is important to note that Options are found by scanning the
 * classpath and are **not** created by Dependency Injection, and so are not able to have dependencies injected into
 * themselves.
 */
public abstract class OrchidOption extends Prioritized {

    /**
     * Return the number of arguments this OrchidOption is expecting. This number is strictly enforced, and the option will
     * only be allowed to parse the command-line option if the number of arguments it finds exactly matches this value.
     *
     * For each custom option that you want to recognize, optionLength must return the number of separate pieces or
     * tokens in the option. For example, we want to be able to use the custom option of the form -tag mytag. This
     * option has two pieces, the -tag option itself and its value, so the optionLength method in our doclet must return
     * 2 for the -tag option.
     *
     * An option can return 0 if it isn't expecting any actual command-line option, but does want to add data to the
     * 'option' object.
     *
     * @return the length of this option, including the flag itself
     */
    public abstract int optionLength();

    /**
     * A callback for when an option on the command-line matches the optionLength or when optionLength is 0. Whatever
     * JSONElement is returned will be available in the root JSONObject at `options.{flag}`.
     *
     * @param options the raw values found on the command line. Its length will always be exactly equal to optionLength
     * @return the data parsed from the command-line option
     */
    public abstract JSONElement parseOption(String[] options);

    /**
     * If this value is not false, this callback is used instead to get a value for this option. Whatever JSONElement is
     * returned will be available in the root JSONObject at `options.{flag}`.
     * @return the default value for this OrchidOption
     */
    public abstract JSONElement getDefaultValue();

    /**
     * The name of the flag used on the command-line. This value should _not_ start with a dash, but options specified
     * on the command-line _must_ begin with a dash. The value returned by `parseOption` is available in the root
     * JSONObject at `options.{flag}`.
     *
     * @return the flag, without any leading dash
     */
    public abstract String getFlag();

    /**
     * Return a description of this OrchidOption, which is displayed when listing available Options.
     *
     * @return this option's description
     */
    public abstract String getDescription();

    /**
     * Return true if this OrchidOption must be set before continuing with the Orchid build.
     *
     * @return true if this option is required, false otherwise.
     */
    public boolean isRequired() {
        return false;
    }
}
