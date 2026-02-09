package com.elixsr.portforwarder.validators;

import android.util.Log;

import com.elixsr.portforwarder.R;
import com.elixsr.portforwarder.exceptions.RuleValidationException;
import com.elixsr.portforwarder.models.RuleModel;
import com.elixsr.portforwarder.util.RuleHelper;
import com.elixsr.portforwarder.util.IpAddressValidator;

/**
 * Created by Cathan on 25/07/2017.
 */
public class RuleModelValidator implements Validator<RuleModel> {

    private static final String TAG = "RuleModelValidator";


    @Override
    public boolean validate(RuleModel ruleModel) throws RuleValidationException {
        return validateRule(ruleModel);
    }

    public static boolean validateRule(RuleModel ruleModel) throws RuleValidationException {
        boolean isValidRuleModel = false;

        if (validateRuleName(ruleModel.getName()) &&
                validateRuleFromPort(ruleModel.getFromPort()) &&
                validateRuleTargetPort(ruleModel.getTargetPort()) &&
                validateRuleTargetIpAddress(ruleModel.getTargetIpAddress()) &&
                validateRuleTargetIpAddressSyntax(ruleModel.getTargetIpAddress())) {
            isValidRuleModel = true;
        }

        return isValidRuleModel;
    }

    public static boolean validateRuleName(String ruleName) throws RuleValidationException {
        if (ruleName == null || ruleName.length() <= 0) {
            throw new RuleValidationException(R.string.error_enter_name);
        }

        return true;
    }

    public static boolean validateRuleFromPort(int ruleFromPort) throws RuleValidationException {
        if (ruleFromPort <= 0 || ruleFromPort < RuleHelper.MIN_PORT_VALUE || ruleFromPort > RuleHelper.MAX_PORT_VALUE) {
            throw new RuleValidationException(R.string.error_from_port_range, RuleHelper.MIN_PORT_VALUE, RuleHelper.MAX_PORT_VALUE);
        }

        return true;
    }

    public static boolean validateRuleFromPort(String ruleFromPort) throws RuleValidationException {
        if (ruleFromPort != null && ruleFromPort.length() > 0) {
            return validateRuleFromPort(Integer.parseInt(ruleFromPort));
        }

        throw new RuleValidationException(R.string.error_from_port_range, RuleHelper.MIN_PORT_VALUE, RuleHelper.MAX_PORT_VALUE);
    }

    public static boolean validateRuleTargetPort(int ruleTargetPort) throws RuleValidationException {
        if (ruleTargetPort <= 0 || ruleTargetPort < RuleHelper.TARGET_MIN_PORT || ruleTargetPort > RuleHelper.MAX_PORT_VALUE) {
            throw new RuleValidationException(R.string.error_target_port_range, RuleHelper.TARGET_MIN_PORT, RuleHelper.MAX_PORT_VALUE);
        }

        return true;
    }

    public static boolean validateRuleTargetPort(String ruleTargetPort) throws RuleValidationException {
        if (ruleTargetPort != null && ruleTargetPort.length() > 0) {
            return validateRuleTargetPort(Integer.parseInt(ruleTargetPort));
        }

        Log.e(TAG, "No target port was included");
        throw new RuleValidationException(R.string.error_target_port_range, RuleHelper.TARGET_MIN_PORT, RuleHelper.MAX_PORT_VALUE);
    }

    public static boolean validateRuleTargetIpAddress(String ruleTargetIpAddress) throws RuleValidationException {
        if (ruleTargetIpAddress != null && ruleTargetIpAddress.length() > 0) {
            return validateRuleTargetIpAddressSyntax(ruleTargetIpAddress);
        }

        throw new RuleValidationException(R.string.error_enter_target_address);
    }

    public static boolean validateRuleTargetIpAddressSyntax(String ruleTargetIpAddress) throws RuleValidationException {
        if (!new IpAddressValidator().validate(ruleTargetIpAddress)) {
            throw new RuleValidationException(R.string.error_target_ip_invalid);
        }

        Log.i(TAG, "validateRuleTargetIpAddressSyntax: TARGET IP VALID");
        return true;
    }

}
