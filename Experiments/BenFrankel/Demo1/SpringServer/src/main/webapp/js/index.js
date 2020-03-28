$(document).ready(function() {
    let newCard = function(data) {
        let card = $('<div>', { 'class': 'card' });

        let header = $('<div>', { 'class': 'header' });
        header.text(data);
        card.append(header);

        let body = $('<div>', { 'class': 'body' });
        card.append(body);

        return card;
    };

    $('input').keypress(function(event) {
        if (event.key == "Enter") {
            event.preventDefault();

            let data = $(this).val();
            $(this).val("");

            $.post({
                url: "add",
                data: { Data: data },
            });

            $(this).parent().parent()
        .before(newCard(data));
        }
    });
});
