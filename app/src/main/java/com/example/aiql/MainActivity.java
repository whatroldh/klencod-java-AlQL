import 'package:flutter/material.dart';

void main() {
  runApp(const HackerDashboard());
}

class HackerDashboard extends StatelessWidget {
  const HackerDashboard({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      theme: ThemeData.dark(useMaterial3: true).copyWith(
        scaffoldBackgroundColor: const Color(0xFF0F0F0F),
        primaryColor: const Color(0xFF00FF41),
        textTheme: const TextTheme(
          bodyMedium: TextStyle(fontFamily: 'Courier New', color: Colors.white),
          titleMedium: TextStyle(fontFamily: 'Courier New', color: Color(0xFF00FF41)),
        ),
      ),
      home: const ControlPanel(),
    );
  }
}

class ControlPanel extends StatefulWidget {
  const ControlPanel({super.key});

  @override
  State<ControlPanel> createState() => _ControlPanelState();
}

class _ControlPanelState extends State<ControlPanel> with SingleTickerProviderStateMixin {
  // Variables
  String _targetUser = "";
  double _progress = 0.0;
  bool _isProcessing = false;
  bool _isMerged = false;
  bool _isClaimed = false;
  List<String> _logs = [];
  
  // Animation Controller for smooth progress bar
  late AnimationController _controller;
  late Animation<double> _animation;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      vsync: this,
      duration: const Duration(seconds: 2), // Total time for fake hack
    );
    
    // Listen to animation updates
    _animation = Tween<double>(begin: 0.0, end: 1.0).animate(_controller)
      ..addListener(() {
        setState(() {
          _progress = _animation.value * 100;
        });
      })
      ..addStatusListener((status) {
        if (status == AnimationStatus.completed) {
          setState(() {
            _isMerged = true;
            _isProcessing = false;
          });
        }
      });
  }

  // Helper to add logs
  void addLog(String msg) {
    setState(() {
      _logs.add("> $msg");
      if (_logs.length > 5) _logs.removeAt(0); // Keep last 5 logs
    });
  }

  void startHack() {
    if (_targetUser.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text("اكتب اسم اليوزر أولاً!"), backgroundColor: Colors.red)
      );
      return;
    }

    setState(() {
      _isProcessing = true;
      _isMerged = false;
      _isClaimed = false;
      _logs.clear();
    });

    // Simulate log messages
    addLog("Connecting to server...");
    Future.delayed(const Duration(milliseconds: 500), () => addLog("Bypassing firewall..."));
    Future.delayed(const Duration(milliseconds: 1200), () => addLog("Injecting payload..."));
    Future.delayed(const Duration(milliseconds: 1800), () => addLog("Access granted."));

    _controller.forward(from: 0.0);
  }

  void claimData() {
    setState(() {
      _isClaimed = true;
    });
    
    // Show success message then move to control panel
    Future.delayed(const Duration(seconds: 1), () {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text("تم نقل الملكية بنجاح!"), backgroundColor: Color(0xFF00FF41))
        );
      }
    });
  }

  void runAction(String action) {
    String msg = "";
    switch(action) {
      case 'photos': msg = "📷 جاري سحب الصور..."; break;
      case 'format': msg = "💾 بدء الفرمة..."; break;
      case 'gps': msg = "📍 الموقع: 34.05° N, 118.24° W"; break;
      case 'lock': msg = "🔒 تم قفل النظام."; break;
    }
    addLog(msg);
    
    if (action == 'format') {
       // Fake format effect
       showDialog(context: context, builder: (_) => AlertDialog(
         backgroundColor: Colors.black,
         content: const Text("جاري حذف البيانات...", style: TextStyle(color: Colors.red)),
       ));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('لوحة التحكم العليا'),
        backgroundColor: Colors.black,
        elevation: 0,
        centerTitle: true,
        iconTheme: const IconThemeData(color: Color(0xFF00FF41)),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            // Step 1: Search Box
            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                border: Border.all(color: const Color(0xFF00FF41)),
                borderRadius: BorderRadius.circular(8),
                color: const Color(0xFF1A1A1A)
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text("1. تحديد الهدف", style: TextStyle(color: Color(0xFF00FF41), fontWeight: FontWeight.bold)),
                  const SizedBox(height: 10),
                  TextField(
                    onChanged: (val) => _targetUser = val,
                    style: const TextStyle(color: Color(0xFF00FF41), fontFamily: 'Courier'),
                    decoration: const InputDecoration(
                      hintText: "أدخل اسم اليوزر أو السوبر كي...",
                      hintStyle: TextStyle(color: Colors.grey),
                      border: OutlineInputBorder(borderSide: BorderSide(color: Colors.grey)),
                    ),
                  ),
                  const SizedBox(height: 10),
                  ElevatedButton(
                    onPressed: _isProcessing ? null : startHack,
                    style: ElevatedButton.styleFrom(
                      backgroundColor: const Color(0xFF00FF41),
                      foregroundColor: Colors.black,
                      minimumSize: const Size(double.infinity, 50),
                    ),
                    child: const Text("بدء الاختراق", style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                  ),
                  if (_logs.isNotEmpty) ...[
                    const SizedBox(height: 10),
                    Container(
                      height: 60,
                      width: double.infinity,
                      padding: const EdgeInsets.all(8),
                      decoration: BoxDecoration(border: Border.all(color: Colors.grey), color: Colors.black),
                      child: SingleChildScrollView(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: _logs.map((log) => Text(log, style: const TextStyle(fontSize: 12, color: Colors.grey))).toList(),
                        ),
                      ),
                    )
                  ]
                ],
              ),
            ),

            const SizedBox(height: 20),

            // Step 2: Merge Progress (Only show if processing or merged)
            if (_isProcessing || _isMerged)
              Expanded(
                flex: 2,
                child: Container(
                  width: double.infinity,
                  padding: const EdgeInsets.all(16),
                  decoration: BoxDecoration(
                    border: Border.all(color: const Color(0xFF00FF41)),
                    borderRadius: BorderRadius.circular(8),
                    color: const Color(0xFF1A1A1A)
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text("2. دمج النظام المستمر", style: TextStyle(color: Color(0xFF00FF41), fontWeight: FontWeight.bold)),
                      const SizedBox(height: 10),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Text(_isProcessing ? "جاري الدمج..." : "تم الدمج"),
                          Text("${_progress.toInt()}%", style: const TextStyle(color: Color(0xFF00FF41)))
                        ],
                      ),
                      const SizedBox(height: 10),
                      LinearProgressIndicator(
                        value: _progress / 100,
                        minHeight: 10,
                        backgroundColor: Colors.grey[900],
                        color: const Color(0xFF00FF41),
                      ),
                    ],
                  ),
                ),
              ),

            // Step 3: Result Box (Shows after merge)
            if (_isMerged && !_isClaimed)
              Expanded(
                flex: 2,
                child: Container(
                  width: double.infinity,
                  padding: const EdgeInsets.all(16),
                  decoration: BoxDecoration(
                    border: Border.all(color: Colors.red),
                    borderRadius: BorderRadius.circular(8),
                    color: const Color(0xFF000000)
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text("3. استلام البيانات", style: TextStyle(color: Color(0xFF00FF41), fontWeight: FontWeight.bold)),
                      const SizedBox(height: 10),
                      const Text("الحساب: ", style: TextStyle(color: Colors.white70)) + 
                      Text(_targetUser, style: const TextStyle(color: Colors.red, fontWeight: FontWeight.bold)) +
                      const SizedBox(height: 10) +
                      const Text("الرصيد: ", style: TextStyle(color: Colors.white70)) + 
                      const Text("\$99,999", style: TextStyle(color: Colors.green, fontWeight: FontWeight.bold)) +
                      const Spacer() +
                      SizedBox(
                        width: double.infinity,
                        child: ElevatedButton(
                          onPressed: claimData,
                          style: ElevatedButton.styleFrom(backgroundColor: Colors.red, foregroundColor: Colors.white),
                          child: const Text("[ OK ] استلام المبلغ والحساب"),
                        ),
                      )
                    ],
                  ),
                ),
              ),

            // Step 4: Control Panel (Shows after claim)
            if (_isClaimed)
              Expanded(
                flex: 3,
                child: Container(
                  width: double.infinity,
                  padding: const EdgeInsets.all(16),
                  decoration: BoxDecoration(
                    border: Border.all(color: const Color(0xFF00FF41)),
                    borderRadius: BorderRadius.circular(8),
                    color: const Color(0xFF1A1A1A)
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text("4. أدوات السيطرة", style: TextStyle(color: Color(0xFF00FF41), fontWeight: FontWeight.bold)),
                      const SizedBox(height: 10),
                      Wrap(
                        spacing: 10,
                        runSpacing: 10,
                        children: [
                          _buildActionBtn("📷 سحب الصور", 'photos'),
                          _buildActionBtn("💾 فرمة الهاتف", 'format'),
                          _buildActionBtn("📍 تحديد الموقع", 'gps'),
                          _buildActionBtn("🔒 قفل النظام", 'lock'),
                        ],
                      ),
                      const Spacer(),
                      Container(
                        height: 80,
                        width: double.infinity,
                        padding: const EdgeInsets.all(8),
                        decoration: BoxDecoration(border: Border.all(color: Colors.grey), color: Colors.black),
                        child: SingleChildScrollView(
                          reverse: true,
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: _logs.map((log) => Text(log, style: const TextStyle(fontSize: 12, color: Colors.grey))).toList(),
                          ),
                        ),
                      )
                    ],
                  ),
                ),
              ),
          ],
        ),
      ),
    );
  }

  Widget _buildActionBtn(String label, String type) {
    return SizedBox(
      width: 140,
      child: OutlinedButton(
        onPressed: () => runAction(type),
        style: OutlinedButton.styleFrom(
          side: const BorderSide(color: Color(0xFF00FF41)),
          foregroundColor: Colors.white,
          backgroundColor: Colors.black,
        ),
        child: Text(label),
      ),
    );
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }
}