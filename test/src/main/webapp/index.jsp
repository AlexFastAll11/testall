<html>
<script type="text/javascript" src="jquery.js"></script>
<script type="text/javascript">
    $(function(){
        $("#test").click(function(){
            var user = [{"age":18,"name":"wxy"}];
            $.ajax({
                type: "POST",
                url: "test1",
                data: JSON.stringify(user),
                success: function(msg){
                    alert( "Data Saved: " + msg );
                }
            });
        });;
    });
</script>
<body>
<h2>Hello World!</h2>
    <button id="test">
        AA
    </button>
</body>
</html>
