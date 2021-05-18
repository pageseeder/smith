[![Maven Central](https://img.shields.io/maven-central/v/org.pageseeder.smith/pso-smith.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.pageseeder.smith%22%20AND%20a:%22pso-smith%22)

# Smith

This library provides a simple and configurable password strengh meter in Java.

It also generates equivalent code in JavaScript so that the client-side rules match the server-side rules without having to interrogate the server constantly.

# Usage

Basic usage:

```java
  // Load the config
  PasswordConfig config = PasswordConfig.load(configFile);
  // Create the password meter
  PasswordMeter meter = new PasswordMeter(config);
  // Get the score
  int score = meter.score(password);
  // OR get the corresponding level
  String level = meter.getLevel(password);
```

The password meter is threadsafe so it can be reused.

# Rationale

Smith evaluate passwords based on a collection of independent rules using a point system or score.

Each rule returns a score for the password. The total score is the score for the password.

The actual score is computed from the raw score using either a function (linear or quadratic) or a manual mapping.

The strength converts the score into a name.

# Configuration

They are defined in XML

```xml
<?xml version="1.0"?>
<password-config version="1.0">
  <rules>
    <rule class="[rule_class1]">
      <property name="[raw_score1]"  value="[actual_score1]"/>
      <property name="[raw_score2]"  value="[actual_score2]"/>
      ...
    </rule>
    <rule class="[rule_class2]">
      <property name="[raw_score1]"  value="[actual_score1]"/>
      <property name="[raw_score2]"  value="[actual_score2]"/>
      ...
    </rule>
    ...
</rules>
  <!-- Defines the thresholds for each level -->
  <levels default="[default_strength]">
    <level name="[strength1]" threshold="[min_score1]"/>
    <level name="[strength2]" threshold="[min_score2]"/>
    ...
  </levels>
</password-config>
```

# Rules

The built-in rules are defined in `org.pageseeder.smith.rule` package and MUST implement the `PasswordRule` interface.

## BannedPasswordRule

Evaluate a password by checking against a list of banned passwords.

The default scoring is as follows:
 * 0 for `null` or empty string `""`
 * 0 for any allowed password
 * -100 for any banned password

The score for banned and allowed passwords can be customized.

## ConsecutiveCharRule

A rule based on the number of identical consecutive characters in the password.

The scoring is as follows:
 * 0 for `null` or empty string `""`
 * 1 point for every character that follows the same character in the password

The maximum value if the length of the password if it is entirely made up of digits.

## DigitCountRule

A rule based on the number of digits [0-9] in the password.

The scoring is as follows:
 * 0 for `null` or empty string `""`
 * 1 point for every digit found in the password

The maximum value if the length of the password if it is entirely made up of digits.

## LengthRule

Evaluate a password based on its length.

The scoring is as follows:
 * 0 for `null` or empty string `""`
 * 1 point for every character found in the password

The score is equals to the length of the password.

## LowerCaseCountRule

A rule based on the number of upper case characters in the password.

The scoring is as follows:
 * 0 for `null` or empty string `""`
 * 1 point for every ASCII lower case letter found in the password</li>

The maximum value if the length of the password if it is entirely made up of lower case letters.

## MixedCaseRule

Evaluate a password based the number of mixed case pairs.

This rule counts the number of lower case letters and the number of upper case letters and returns the minimum value.

The scoring is as follows:
 * 0 for `null` or empty string `""`
 * 1 point per pair of lower/upper case letters found

The maximum value is half the password length if the password is made up of half upper letters and half lower case letters

## MixedCharRule

Evaluate a password based the number of mixed characters.

This rule considers three kind of characters, ASCII letters `[a-zA-Z]`, digits `[0-9]` and any other characters.

The scoring is as follows:
 * 0 for `null` or empty string `""`
 * 1 point if the password is made up of one kind of characters
 * 2 points if the password is made up of two kinds of characters
 * 3 points if the password is made up of three kinds of characters

## QwertyConsecutiveCharRule

Evaluate a password by checking whether it follows a sequence from a QWERTY keyboard.

## RepeatedCharRule

A rule based on the number of repeated characters in the password.

The scoring is as follows:
 * 0 for `null` or empty string `""`
 * 1 point for repeated character in the password

The maximum value is the length of the password minus 1 if the password only contains a single character repeated multiple times.

## SpecialCharCountRule

A rule based on the number of upper case characters in the password.

The scoring is as follows:
 * 0 for `null` or empty string `""`
 * 1 point for every special character found in the password

The maximum value is the length of the password if it is entirely made up of special characters.

## UniqueCharRule

A rule based on the number of unique characters in the password.

The scoring is as follows:
 * 0 for `null` or empty string `""`
 * 1 point for every unique character in the password

The maximum value if the length of the password if it is entirely made up of unique characters. In other words, it is the length of the password if no character is repeated.

## UpperCaseCountRule

A rule based on the number of upper case characters in the password.

The scoring is as follows: UpperCaseCountRule
 * 0 for `null` or empty string `""`
 * 1 point for every ASCII upper case letter found in the password

The maximum value if the length of the password if it is entirely made up of uppercase letters.

