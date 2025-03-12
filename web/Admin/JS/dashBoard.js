google.charts.load('current', {'packages':['corechart']});
    
    google.charts.setOnLoadCallback(drawColumnChart);
    google.charts.setOnLoadCallback(drawLineChart);

    function drawColumnChart() {
        try {
            var jsonData = '<c:out value="${categorySalesJson}" escapeXml="false"/>';
            var categorySales = JSON.parse(jsonData);
            
            if (!categorySales || categorySales.length === 0) {
                document.getElementById('chart_column').innerHTML = "Không có dữ liệu để hiển thị";
                return;
            }

            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Danh mục');
            data.addColumn('number', 'Doanh số');

            categorySales.forEach(function(item) {
                data.addRow([item.categoryName, parseFloat(item.sales)]);
            });

            var options = {
                title: 'DOANH SỐ THEO DANH MỤC',
                chartArea: {width: '60%'},
                colors: ['#5b62f4'],
                vAxis: {minValue: 0}
            };

            var chart = new google.visualization.ColumnChart(document.getElementById('chart_column'));
            chart.draw(data, options);
        } catch (e) {
            console.error('Error drawing column chart:', e);
            document.getElementById('chart_column').innerHTML = "Lỗi khi vẽ biểu đồ";
        }
    }

    function drawLineChart() {
        try {
            var jsonData = '<c:out value="${revenueDataJson}" escapeXml="false"/>';
            var revenueData = JSON.parse(jsonData);
            
            if (!revenueData || revenueData.length === 0) {
                document.getElementById('chart_line').innerHTML = "Không có dữ liệu để hiển thị";
                return;
            }

            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Tháng');
            data.addColumn('number', 'Doanh thu');

            revenueData.forEach(function(item) {
                data.addRow([item.date, parseFloat(item.revenue)]);
            });

            var options = {
                title: 'DOANH THU THEO THÁNG',
                curveType: 'function',
                legend: {position: 'bottom'},
                colors: ['#2dd36f'],
                vAxis: {minValue: 0}
            };

            var chart = new google.visualization.LineChart(document.getElementById('chart_line'));
            chart.draw(data, options);
        } catch (e) {
            console.error('Error drawing line chart:', e);
            document.getElementById('chart_line').innerHTML = "Lỗi khi vẽ biểu đồ";
        }
    }