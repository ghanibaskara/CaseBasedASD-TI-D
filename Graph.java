package CaseBased;

class Edge {
    private String nodeA;
    private String nodeB;
    private double jarak;
    private int waktu;

    public Edge(String nodeA, String nodeB, double jarak, int waktu) {
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.jarak = jarak;
        this.waktu = waktu;
    }

    public String getNodeA() {
        return nodeA;
    }

    public String getNodeB() {
        return nodeB;
    }

    public double getJarak() {
        return jarak;
    }

    public int getWaktu() {
        return waktu;
    }

    @Override
    public String toString() {
        return nodeA + " <-> " + nodeB + " | Jarak: " + jarak + " km | Waktu: " + waktu + " menit";
    }
}

class Graph {
    private int maxNodes;
    private String[] nodeNames;
    private double[][] distances;
    private int[][] times;
    private int nodeCount;
    private Edge[] edges;
    private int edgeCount;

    public Graph(int maxNodes) {
        this.maxNodes = maxNodes;
        this.nodeNames = new String[maxNodes];
        this.distances = new double[maxNodes][maxNodes];
        this.times = new int[maxNodes][maxNodes];
        this.nodeCount = 0;
        this.edges = new Edge[maxNodes * maxNodes];
        this.edgeCount = 0;

        for (int i = 0; i < maxNodes; i++) {
            for (int j = 0; j < maxNodes; j++) {
                distances[i][j] = -1;
                times[i][j] = -1;
            }
        }
    }

    private int getIndex(String name) {
        for (int i = 0; i < nodeCount; i++) {
            if (nodeNames[i].equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    public boolean isLokasiAda(String name) {
        return getIndex(name) != -1;
    }

    public boolean isLokasiDuplikat(String name) {
        for (int i = 0; i < nodeCount; i++) {
            if (nodeNames[i].equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public void tampilkanDaftarLokasi() {
        System.out.println("\n--- Daftar Lokasi yang Tersedia ---");
        if (nodeCount == 0) {
            System.out.println("Tidak ada lokasi yang tersedia.");
        } else {
            for (int i = 0; i < nodeCount; i++) {
                System.out.println((i + 1) + ". " + nodeNames[i]);
            }
        }
        System.out.println();
    }

    public void tambahLokasi(String name) {
        if (nodeCount < maxNodes) {
            nodeNames[nodeCount] = name;
            nodeCount++;
        }
    }

    public Edge[] getEdges() {
        Edge[] result = new Edge[edgeCount];
        for (int i = 0; i < edgeCount; i++) {
            result[i] = edges[i];
        }
        return result;
    }

    public int getEdgeCount() {
        return edgeCount;
    }

    public void tambahJalur(String nodeA, String nodeB, double jarak, int waktu) {
        int idxA = getIndex(nodeA);
        int idxB = getIndex(nodeB);

        if (idxA != -1 && idxB != -1) {
            distances[idxA][idxB] = jarak;
            distances[idxB][idxA] = jarak;
            times[idxA][idxB] = waktu;
            times[idxB][idxA] = waktu;
            
            if (edgeCount < edges.length) {
                edges[edgeCount] = new Edge(nodeA, nodeB, jarak, waktu);
                edgeCount++;
            }
        }
    }

    public boolean bisaPergi(String start, String end) {
        int startIdx = getIndex(start);
        int endIdx = getIndex(end);

        if (startIdx == -1 || endIdx == -1) return false;

        int[] queue = new int[maxNodes];
        int head = 0;
        int tail = 0;
        boolean[] visited = new boolean[maxNodes];

        queue[tail++] = startIdx;
        visited[startIdx] = true;

        while (head < tail) {
            int current = queue[head++];
            if (current == endIdx) return true;

            for (int i = 0; i < nodeCount; i++) {
                if (distances[current][i] != -1 && !visited[i]) {
                    visited[i] = true;
                    queue[tail++] = i;
                }
            }
        }
        return false;
    }

    public void pergi(String start, String end) {
        int startIdx = getIndex(start);
        int endIdx = getIndex(end);

        solveBFS(startIdx, endIdx);
        solveDFS(startIdx, endIdx);
    }

    private void solveBFS(int startIdx, int endIdx) {
        int[] queue = new int[maxNodes];
        int head = 0;
        int tail = 0;
        boolean[] visited = new boolean[maxNodes];
        int[] parent = new int[maxNodes];

        for (int i = 0; i < maxNodes; i++) parent[i] = -1;

        queue[tail++] = startIdx;
        visited[startIdx] = true;
        boolean found = false;

        while (head < tail) {
            int current = queue[head++];
            if (current == endIdx) {
                found = true;
                break;
            }

            for (int i = 0; i < nodeCount; i++) {
                if (distances[current][i] != -1 && !visited[i]) {
                    visited[i] = true;
                    parent[i] = current;
                    queue[tail++] = i;
                }
            }
        }

        if (found) {
            printPath("BFS", parent, startIdx, endIdx);
        }
    }

    private void solveDFS(int startIdx, int endIdx) {
        boolean[] visited = new boolean[maxNodes];
        int[] pathStack = new int[maxNodes];
        int top = -1;
        
        pathStack[++top] = startIdx;
        visited[startIdx] = true;
        
        dfsRecursive(startIdx, endIdx, visited, pathStack, top);
    }

    private boolean dfsRecursive(int current, int target, boolean[] visited, int[] pathStack, int top) {
        if (current == target) {
            printPathArray("DFS", pathStack, top + 1);
            return true;
        }

        for (int i = 0; i < nodeCount; i++) {
            if (distances[current][i] != -1 && !visited[i]) {
                visited[i] = true;
                pathStack[top + 1] = i;
                if (dfsRecursive(i, target, visited, pathStack, top + 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void printPath(String method, int[] parent, int start, int end) {
        int[] path = new int[maxNodes];
        int count = 0;
        int curr = end;
        
        while (curr != -1) {
            path[count++] = curr;
            if (curr == start) break;
            curr = parent[curr];
        }

        int[] reversedPath = new int[count];
        for (int i = 0; i < count; i++) {
            reversedPath[i] = path[count - 1 - i];
        }
        
        printPathArray(method, reversedPath, count);
    }

    private void printPathArray(String method, int[] path, int length) {
        double totalDist = 0;
        int totalTime = 0;
        String routeStr = "";

        for (int i = 0; i < length; i++) {
            routeStr += nodeNames[path[i]];
            if (i < length - 1) {
                routeStr += " -> ";
                totalDist += distances[path[i]][path[i+1]];
                totalTime += times[path[i]][path[i+1]];
            }
        }

        System.out.println(method + " Route:");
        System.out.println("  Jalur: " + routeStr);
        System.out.println("  Total Jarak: " + totalDist + " km");
        System.out.println("  Total Waktu: " + totalTime + " menit");
    }

    public void jalurTerpendek(String start, String end) {
        int startIdx = getIndex(start);
        int endIdx = getIndex(end);

        if (startIdx == -1 || endIdx == -1) return;

        double[] dist = new double[maxNodes];
        boolean[] visited = new boolean[maxNodes];
        int[] parent = new int[maxNodes];

        for (int i = 0; i < nodeCount; i++) {
            dist[i] = Double.MAX_VALUE;
            parent[i] = -1;
            visited[i] = false;
        }

        dist[startIdx] = 0;

        for (int count = 0; count < nodeCount - 1; count++) {
            int u = -1;
            double min = Double.MAX_VALUE;

            for (int v = 0; v < nodeCount; v++) {
                if (!visited[v] && dist[v] <= min) {
                    min = dist[v];
                    u = v;
                }
            }

            if (u == -1) break;
            visited[u] = true;

            for (int v = 0; v < nodeCount; v++) {
                if (!visited[v] && distances[u][v] != -1 && 
                    dist[u] != Double.MAX_VALUE && 
                    dist[u] + distances[u][v] < dist[v]) {
                    dist[v] = dist[u] + distances[u][v];
                    parent[v] = u;
                }
            }
        }

        if (dist[endIdx] != Double.MAX_VALUE) {
            System.out.println("Dijkstra Route (Shortest Path):");
            
            int[] path = new int[maxNodes];
            int count = 0;
            int curr = endIdx;
            
            while (curr != -1) {
                path[count++] = curr;
                if (curr == startIdx) break;
                curr = parent[curr];
            }
            
            int[] reversedPath = new int[count];
            for (int i = 0; i < count; i++) {
                reversedPath[i] = path[count - 1 - i];
            }
            
            String routeStr = "";
            for (int i = 0; i < count; i++) {
                routeStr += nodeNames[reversedPath[i]];
                if (i < count - 1) routeStr += " -> ";
            }
            
            System.out.println("  Jalur: " + routeStr);
            System.out.println("  Total Jarak: " + dist[endIdx] + " km");
        }
    }

    public void tampilkanInfo() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("INFORMASI GRAF");
        System.out.println("=".repeat(50));
        System.out.println("Jumlah Lokasi: " + nodeCount);
        
        if (nodeCount == 0) {
            System.out.println("Tidak ada lokasi yang tersedia.");
            System.out.println("=".repeat(50));
            return;
        }
        
        System.out.println("\n--- Daftar Lokasi ---");
        for (int i = 0; i < nodeCount; i++) {
            System.out.println((i + 1) + ". " + nodeNames[i]);
        }
        
        System.out.println("\n--- Daftar Jalur ---");
        int jalurCount = 0;
        for (int i = 0; i < nodeCount; i++) {
            for (int j = i + 1; j < nodeCount; j++) {
                if (distances[i][j] != -1) {
                    jalurCount++;
                    System.out.println(jalurCount + ". " + nodeNames[i] + " <-> " + nodeNames[j] + 
                                     " | Jarak: " + distances[i][j] + " km | Waktu: " + times[i][j] + " menit");
                }
            }
        }
        
        if (jalurCount == 0) {
            System.out.println("Tidak ada jalur yang tersedia.");
        } else {
            System.out.println("\nTotal Jalur: " + jalurCount);
        }
        System.out.println("=".repeat(50));
    }

    public void tampilkanSemuaRute() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("MENAMPILKAN SEMUA RUTE");
        System.out.println("=".repeat(50));
        
        if (nodeCount == 0) {
            System.out.println("Error: Tidak ada lokasi yang tersedia!");
            System.out.println("=".repeat(50));
            return;
        }
        
        if (nodeCount == 1) {
            System.out.println("Error: Minimal harus ada 2 lokasi untuk mencari rute!");
            System.out.println("=".repeat(50));
            return;
        }
        
        int totalRute = 0;
        int ruteDitemukan = 0;
        
        for (int i = 0; i < nodeCount; i++) {
            for (int j = 0; j < nodeCount; j++) {
                if (i != j) {
                    totalRute++;
                    String asal = nodeNames[i];
                    String tujuan = nodeNames[j];
                    
                    System.out.println("\n------- Rute ke-" + totalRute + " (" + asal + " -> " + tujuan + ") -------");
                    
                    if (bisaPergi(asal, tujuan)) {
                        ruteDitemukan++;
                        pergi(asal, tujuan);
                        jalurTerpendek(asal, tujuan);
                    } else {
                        System.out.println("Status: Tidak dapat dijangkau");
                    }
                }
            }
        }
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("RINGKASAN HASIL");
        System.out.println("=".repeat(50));
        System.out.println("Total Rute Dicari: " + totalRute);
        System.out.println("Rute Ditemukan: " + ruteDitemukan);
        System.out.println("Rute Tidak Ditemukan: " + (totalRute - ruteDitemukan));
        System.out.println("=".repeat(50));
    }
}
