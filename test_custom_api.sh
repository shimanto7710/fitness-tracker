#!/bin/bash

echo "🧪 Testing Custom Food Detection API"
echo "===================================="

echo "📡 Testing API endpoint: https://cuddly-umbrella-rr7qvrgq5g92xx7p-8000.app.github.dev/predict"
echo ""

# Test with a simple image file (you can replace this with an actual image)
echo "📡 Testing API with form-data..."
curl -s -X POST \
     -F "file=@/dev/null" \
     "https://cuddly-umbrella-rr7qvrgq5g92xx7p-8000.app.github.dev/predict" | head -20

echo ""
echo ""
echo "✅ API testing completed!"
echo ""
echo "🔍 Expected results:"
echo "   - If API is accessible: You'll see a response"
echo "   - If API is down: You'll see an error or timeout"
echo "   - If API needs authentication: You'll see 401/403 errors"
echo ""
echo "💡 This API should accept image files and return food detection results!"
