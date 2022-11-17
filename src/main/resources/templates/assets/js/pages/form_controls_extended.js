$(function () {
    $('[name="format-date"]').formatter({
        pattern: '{{99}}/{{99}}/{{9999}}'
    });
    $('[name="format-credit-card"]').formatter({
        pattern: '{{9999}} - {{9999}} - {{9999}} - {{9999}}'
    });
    $('.format-phone-number').formatter({
        pattern: '({{999}}) {{999}} - {{9999}}'
    });
    $('[name="format-phone-ext"]').formatter({
        pattern: '({{999}}) {{999}} - {{9999}} / {{a999}}'
    });
    $('[name="format-currency"]').formatter({
        pattern: '${{999}}.{{99}}'
    });
    $('[name="format-international-phone"]').formatter({
        pattern: '+3{{9}} {{999}} {{999}} {{999}}'
    });
    $('[name="format-tax-id"]').formatter({
        pattern: '{{99}} - {{9999999}}'
    });
    $('[name="format-ssn"]').formatter({
        pattern: '{{999}} - {{99}} - {{9999}}'
    });
    $('[name="format-product-key"]').formatter({
        pattern: '{{a*}} - {{999}} - {{a999}}'
    });
    $('[name="format-order-number"]').formatter({
        pattern: '{{aaa}} - {{999}} - {{***}}'
    });
    $('[name="format-isbn"]').formatter({
        pattern: '{{999}} - {{99}} - {{999}} - {{9999}} - {{9}}'
    });
    $('[name="format-persistent"]').formatter({
        pattern: '+3 ({{999}}) {{999}} - {{99}} - {{99}}'
    });
    $('.maxlength').maxlength();
    $('.maxlength-threshold').maxlength({
        threshold: 15
    });
    $('.maxlength-custom').maxlength({
        threshold: 10,
        warningClass: "label label-primary",
        limitReachedClass: "label label-danger"
    });
    $('.maxlength-options').maxlength({
        alwaysShow: true,
        threshold: 10,
        warningClass: "label label-success",
        limitReachedClass: "label label-danger",
        separator: ' of ',
        preText: 'You have ',
        postText: ' chars remaining.',
        validate: true
    });
    $('.maxlength-textarea').maxlength({
        alwaysShow: true
    });
    $('.maxlength-label-position').maxlength({
        alwaysShow: true,
        warningClass: "label label-danger",
        placement: 'top'
    });
    var substringMatcher = function (strs) {
        return function findMatches(q, cb) {
            var matches, substringRegex;
            matches = [];
            substrRegex = new RegExp(q, 'i');
            $.each(strs, function (i, str) {
                if (substrRegex.test(str)) {
                    matches.push({value: str});
                }
            });
            cb(matches);
        };
    };
});